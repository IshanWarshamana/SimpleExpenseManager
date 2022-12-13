package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;




public class DatabaseSQLiteHelp extends SQLiteOpenHelper {



    private static final String DB_NAME="200695K.db";
    private static final int VERSION=1;
    protected Context context;

    //accounts table
    public static final String ACCOUNTS = "ACCOUNTS";
    //column names of accounts table
    public static final String ID = "ID";
    public static final String ACC_NO = "ACC_NO";
    public static final String BANK_NAME = "BANK_NAME";
    public static final String ACC_HOLDER_NAME = "ACC_HOLDER_NAME";
    public static final String BALANCE = "BALANCE";


    //transactions table
    public static final String TRANSACTIONS = "TRANSACTIONS";
    //column names of transactions table
    public static final String TRANSACTION_ID = "TRANSACTION_ID";
    public static final String DATE = "DATE";
    public static final String ACCOUNT_NO = "ACCOUNT_NO";
    public static final String EXPENSE_TYPE = "EXPENSE_TYPE";
    public static final String AMOUNT = "AMOUNT";


    public DatabaseSQLiteHelp(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccountQuery = " CREATE TABLE " + ACCOUNTS +
                "( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ACC_NO + " TEXT," +
                BANK_NAME + " TEXT," +
                ACC_HOLDER_NAME + " TEXT," +
                BALANCE + " REAL"+
                ");";


        String createTransactionQuery= " CREATE TABLE " + TRANSACTIONS +
                " (" +
                TRANSACTION_ID + " TEXT," +
                DATE + " TEXT," +
                ACCOUNT_NO + " TEXT," +
                EXPENSE_TYPE + " TEXT," +
                AMOUNT + " REAL" +
                ");";


        sqLiteDatabase.execSQL(createAccountQuery);
        sqLiteDatabase.execSQL(createTransactionQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACCOUNTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRANSACTIONS);

        // create new tables
        onCreate(sqLiteDatabase);
    }
}
