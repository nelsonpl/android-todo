package com.npx.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.nio.DoubleBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoBus {

    private DbHelper dbHelper;

    public TodoBus(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public List<Todo> list() {
        return list(false);
    }

    public List<Todo> listComplete() {
        return list(true);
    }

    private List<Todo> list(boolean isComplete) {
        List<Todo> list = null;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DbHelper.TABLE_ID,
                DbHelper.TABLE_TITLE,
                DbHelper.TABLE_CREATE,
                DbHelper.TABLE_WHEN,
        };

        String selection = DbHelper.TABLE_STATUS + (isComplete ? " = 2" : " = 1");

        Cursor cursor = db.query(
                DbHelper.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {

            list = new ArrayList<>();

            while (cursor.moveToNext()) {

                Long id = cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_ID));
                String title = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_TITLE));
                Long create = cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_CREATE));
                Long when = cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_WHEN));

                Todo item = new Todo();
                item.setId(id);
                item.setTitle(title);
                item.setCreate(create);
                if (when > 0)
                    item.setWhen(when);
                list.add(item);
            }
        }
        cursor.close();

        return list;
    }

    public long save(Todo entity) {

        long id;

        Calendar c = Calendar.getInstance();

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DbHelper.TABLE_TITLE, entity.getTitle());
        values.put(DbHelper.TABLE_CREATE, c.getTimeInMillis());
        values.put(DbHelper.TABLE_WHEN, entity.getWhen());

        if (entity.getId() == 0) {
            id = db.insert(DbHelper.TABLE_NAME, null, values);
        } else {
            values.put(DbHelper.TABLE_ID, entity.getId());
            id = db.update(DbHelper.TABLE_NAME, values, DbHelper.TABLE_ID + " =?", new String[]{Long.toString(entity.getId())});
        }
        return id;
    }

    public Todo get(Long id) {

        Todo entity = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DbHelper.TABLE_TITLE,
                DbHelper.TABLE_CREATE,
                DbHelper.TABLE_WHEN,
        };

        String selection = DbHelper.TABLE_ID + " = ?";
        String[] selectionArgs = new String[]{Long.toString(id)};

        Cursor cursor = db.query(
                DbHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String title = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_TITLE));
                Long create = cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_CREATE));
                Long when = cursor.getLong(cursor.getColumnIndex(DbHelper.TABLE_WHEN));

                entity = new Todo();
                entity.setId(id);
                entity.setTitle(title);
                entity.setCreate(create);
                if (when > 0)
                    entity.setWhen(when);
            }
        }
        cursor.close();

        return entity;

    }

    public void delete(Long id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_NAME, DbHelper.TABLE_ID + "=?", new String[]{Long.toString(id)});

    }

    public void complete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbHelper.TABLE_STATUS, 2);
        values.put(DbHelper.TABLE_ID, id);
        db.update(DbHelper.TABLE_NAME, values, DbHelper.TABLE_ID + " =?", new String[]{Long.toString(id)});
    }

    public void clear() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_NAME, DbHelper.TABLE_STATUS + "=?", new String[]{"2"});
    }
}
