package com.example.akanksha.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "item_db";
    public static final int VERSION = 1;


    private static ItemOpenHelper instance;

    public static ItemOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new ItemOpenHelper(context.getApplicationContext());
        }
        return instance;
    }


    public ItemOpenHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String expensesSql = "CREATE TABLE " + Contract.Item.TABLE_NAME  + " (  " +
                Contract.Item.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Contract.Item.COLUMN_TITLE  + " TEXT , " +
                Contract.Item.COLUMN_DESC + " TEXT , " +
                Contract.Item.COLUMN_DATE + " TEXT , " +
                Contract.Item.COLUMN_TIME + " TEXT , " +
                Contract.Item.COLUMN_CATEGORY + " TEXT , " +
                Contract.Item.COLUMN_MARK + " INTEGER , " +
                Contract.Item.COLUMN_CHECK + " INTEGER )";


        sqLiteDatabase.execSQL(expensesSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
