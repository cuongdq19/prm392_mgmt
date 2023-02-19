package com.example.fruitmanagement.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.fruitmanagement.constants.Constants;


public class MyDatabase extends SQLiteOpenHelper {

    public MyDatabase(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_FRUIT_QUERY);
        db.execSQL(Constants.SEED_FRUIT_QUERY);

        db.execSQL(Constants.CREATE_USER_QUERY);
        db.execSQL(Constants.SEED_USER_QUERY);

        db.execSQL(Constants.CREATE_CART_QUERY);

        db.execSQL(Constants.CREATE_ORDER_QUERY);
        db.execSQL(Constants.CREATE_ORDERDETAIL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constants.DROP_FRUIT_QUERY);
        db.execSQL(Constants.DROP_USER_QUERY);
        db.execSQL(Constants.DROP_CART_QUERY);
        db.execSQL(Constants.DROP_ORDER_QUERY);
        db.execSQL(Constants.DROP_ORDERDETAIL_QUERY);

        onCreate(db);
    }
}

