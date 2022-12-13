package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DatabaseSQLiteHelp;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO extends DatabaseSQLiteHelp implements TransactionDAO {
    public PersistentTransactionDAO(Context context) {
        super (context);


    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase myDB = getWritableDatabase();

        ContentValues accContents = new ContentValues();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateAsString = df.format(date);

        accContents.put(DATE, dateAsString);
        accContents.put(ACCOUNT_NO, accountNo);
        accContents.put(EXPENSE_TYPE, String.valueOf(expenseType));
        accContents.put(AMOUNT, amount);

        myDB.insert(TRANSACTIONS, null, accContents);
        myDB.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase myDB = getReadableDatabase();
        String get_All_query = "SELECT * FROM " + TRANSACTIONS;
        Cursor cursor = myDB.rawQuery(get_All_query,null);

        if (cursor.moveToFirst()){
            do{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date day = null;
                try {
                    day = sdf.parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ExpenseType expense_type;
                if(Objects.equals(cursor.getString(3), "EXPENSE")){
                    expense_type = ExpenseType.EXPENSE;
                }else{
                    expense_type = ExpenseType.INCOME;
                }

                Transaction transaction = new Transaction(day, cursor.getString(2), expense_type,cursor.getDouble(4));
                transactionList.add(transaction);
            }while(cursor.moveToNext());
        }
        myDB.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase myDB = getReadableDatabase();
        String get_All_query = "SELECT * FROM " + TRANSACTIONS;
        Cursor cursor = myDB.rawQuery(get_All_query,null);

        if (cursor.moveToFirst()){
            int i = 0;
            do{
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date day = null;
                try {
                    day = sdf.parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ExpenseType expenseType;
                if(Objects.equals(cursor.getString(3), "EXPENSE")){
                    expenseType = ExpenseType.EXPENSE;
                }else{
                    expenseType = ExpenseType.INCOME;
                }

                Transaction trans = new Transaction(day, cursor.getString(2), expenseType,cursor.getDouble(4));
                transactionList.add(trans);
                i += 1;
            }while(cursor.moveToNext() && i<limit);
        }
        myDB.close();
        return transactionList;
    }
}
