package com.example.translationapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "suggestions.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SUGGESTIONS = "suggestions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_CONTACT = "contact";

    private static final String CREATE_TABLE_SUGGESTIONS =
            "CREATE TABLE " + TABLE_SUGGESTIONS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_CONTACT + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SUGGESTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUGGESTIONS);
        onCreate(db);
    }
}