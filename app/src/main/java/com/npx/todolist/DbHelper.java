package com.npx.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final Integer DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TodoListDb.db";

    public static final String TABLE_NAME = "TODO";
    public static final String TABLE_ID = "ID";
    public static final String TABLE_TITLE = "TITLE";
    public static final String TABLE_STATUS = "STATUS";
    public static final String TABLE_CREATE = "DT_CREATE";
    public static final String TABLE_WHEN = "DT_WHEN";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE '" + TABLE_NAME + "' ("
                    + "'" + TABLE_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "'" + TABLE_TITLE + "' TEXT, "
                    + "'" + TABLE_STATUS + "' INTEGER DEFAULT 1, "
                    + "'" + TABLE_CREATE + "' INTEGER, "
                    + "'" + TABLE_WHEN + "' INTEGER );";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_CREATE_TABLE);
    }

}
