package com.srbenicio.slist.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.srbenicio.slist.creators.DatabaseCreator;
import com.srbenicio.slist.creators.GroupTable;

import java.util.Date;

public class DatabaseGroupController {
	private SQLiteDatabase db;
    private final DatabaseCreator database;

    ContentValues values;
    long result;

    public DatabaseGroupController(Context context){
        database = new DatabaseCreator(context);
    }

    public boolean insert(String name, String imageUri) {
        db = database.getWritableDatabase();

        values = new ContentValues();
        values.put(GroupTable.COLUMN_NAME, name);
        values.put(GroupTable.COLUMN_IMAGE, imageUri);  // Store image URI as a String
        values.put(GroupTable.COLUMN_CREATED_IN, new Date().toString());

        result = db.insert(GroupTable.TABLE_NAME, null, values);
        db.close();

        return (result != -1);
    }

    public Cursor loadData() {
        Cursor cursor;
        String[] fields = {GroupTable.COLUMN_ID, GroupTable.COLUMN_NAME, GroupTable.COLUMN_IMAGE};

        db = database.getReadableDatabase();
        cursor = db.query(GroupTable.TABLE_NAME, fields, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public boolean delete(int id){
        String where = GroupTable.COLUMN_ID + "=" + id;
        db = database.getReadableDatabase();
        int result=db.delete(GroupTable.TABLE_NAME,where,null);
        db.close();

        return result != -1;
    }

}
