package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseSQLiteHelp;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO extends DatabaseSQLiteHelp implements AccountDAO {

    private final DatabaseSQLiteHelp helper;
    private SQLiteDatabase db;




    public PersistentAccountDAO(Context context) {
        super(context);
        helper = new DatabaseSQLiteHelp(context);
    }

    @Override
    public List<String> getAccountNumbersList() {


        SQLiteDatabase myDatabase = getReadableDatabase();
        String GetQuery = "SELECT * FROM " + ACCOUNTS;
        Cursor cursor = myDatabase.rawQuery(GetQuery,null);
        List<String> AccNumbersList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                AccNumbersList.add(cursor.getString(1));
                System.out.println(cursor.getString(1));
            }while(cursor.moveToNext());
        }
        myDatabase.close();
        return AccNumbersList;
    }







    @Override
    public List<Account> getAccountsList(){


        System.out.println("Get Accounts");
        SQLiteDatabase myDatabase = getReadableDatabase();
        String GetQuery = "SELECT * FROM " + ACCOUNTS;
        Cursor cursor = myDatabase.rawQuery(GetQuery,null);
        List<Account> AccList = new ArrayList<>();

        if (cursor.moveToFirst()){
            do{
                System.out.println("moved");
                Account account = new Account(cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),cursor.getDouble(4));
                AccList.add(account);
            }while(cursor.moveToNext());
        }
        myDatabase.close();
        return AccList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        SQLiteDatabase myDatabase = getReadableDatabase();
        String GetQuery = "SELECT * FROM " + ACCOUNTS+ " WHERE " + ACCOUNT_NO + "=" + accountNo;
        Cursor cursor = myDatabase.rawQuery(GetQuery,null);

        if (cursor.moveToFirst()){
            account = new Account(cursor.getString(1),
                    cursor.getString(2),cursor.getString(3),cursor.getDouble(4));
        } else{
            throw new InvalidAccountException("Invalid Account");
        }
        myDatabase.close();
        return account;

    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase myDatabase = getWritableDatabase();
        ContentValues accContent = new ContentValues();
        accContent.put(ACCOUNT_NO, account.getAccountNo());
        accContent.put(BANK_NAME, account.getBankName());
        accContent.put(ACC_HOLDER_NAME, account.getAccountHolderName());
        accContent.put(BALANCE, account.getBalance());
        myDatabase.insert(ACCOUNTS, null, accContent);
        myDatabase.close();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase myDatabase = getReadableDatabase();
        myDatabase.delete(ACCOUNTS, ACCOUNT_NO + "=? ", new String[]{String.valueOf(accountNo)});
        myDatabase.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        db = helper.getWritableDatabase();
        String[] projection = {
                BALANCE
        };

        String selection = ACCOUNT_NO + " = ?";
        String[] selectionArgs = { accountNo };

        Cursor cursor = db.query(
                ACCOUNTS,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        double balance;
        if(cursor.moveToFirst())
            balance = cursor.getDouble(0);
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(BALANCE, balance - amount);
                break;
            case INCOME:
                values.put(BALANCE, balance + amount);
                break;
        }

        // updating row
        db.update(ACCOUNTS, values, ACCOUNT_NO + " = ?",
                new String[] { accountNo });

        cursor.close();
        db.close();

    }
}