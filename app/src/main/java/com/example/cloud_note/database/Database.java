package com.example.cloud_note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    static final String db_NAME = "login.db";
    static final int VERSION = 1;

    public Database(Context context) {
        super(context, db_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTb = "CREATE TABLE Login(idUSER INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,jwt TEXT  );";
        sqLiteDatabase.execSQL(createTb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
