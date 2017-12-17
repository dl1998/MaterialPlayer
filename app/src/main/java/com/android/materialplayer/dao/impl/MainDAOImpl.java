package com.android.materialplayer.dao.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.android.materialplayer.dao.MainDAO;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dl1998 on 07.12.17.
 */

abstract class MainDAOImpl<T> implements MainDAO<T> {

    private final String TABLE_NAME;

    private SQLiteDatabase db;

    public MainDAOImpl(SQLiteDatabase db, String tableName) {
        this.db = db;
        this.TABLE_NAME = tableName;
    }

    abstract T getByCursor(Cursor cursor);

    abstract SQLiteStatement bindAdd(SQLiteStatement statement, T object);

    abstract SQLiteStatement bindUpdate(SQLiteStatement statement, T object);

    List<T> get(String selection, String[] selectionArgs) {
        List<T> objectsList = new LinkedList<>();

        Cursor cursor;

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    objectsList.add(getByCursor(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            db.endTransaction();
        }

        if (cursor != null) cursor.close();

        return objectsList;
    }

    private void removeByQuery(String SQL) {
        SQLiteStatement statement = db.compileStatement(SQL);
        db.beginTransaction();
        try {
            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void add(T object, String SQL) {
        SQLiteStatement statement = db.compileStatement(SQL);

        db.beginTransaction();
        try {
            statement = bindAdd(statement, object);

            statement.execute();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void update(T object, String SQL) {
        SQLiteStatement statement = db.compileStatement(SQL);

        db.beginTransaction();
        try {
            statement = bindUpdate(statement, object);

            statement.executeUpdateDelete();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void remove(String SQL) {
        removeByQuery(SQL);
    }

    @Override
    public T findById(String selection, Long id) {
        List<T> list = get(selection, new String[]{String.valueOf(id)});
        return list == null ? null : list.get(0);
    }

    @Override
    public List<T> getAll() {
        return get(null, null);
    }
}
