package com.srbenicio.slist.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.srbenicio.slist.creators.DatabaseCreator;
import com.srbenicio.slist.creators.GroupTable;
import com.srbenicio.slist.creators.ItemTable;

import java.util.Date;

public class DatabaseGroupController {
	private SQLiteDatabase db;
    private final DatabaseCreator database;

    ContentValues values;
    long result;

    public DatabaseGroupController(Context context){
        database = DatabaseCreator.getInstance(context);
    }

    public boolean insert(String name, String imageUri) {
        db = database.getWritableDatabase();

        if (name.isEmpty()) return false;

        values = new ContentValues();
        values.put(GroupTable.COLUMN_NAME, name);
        values.put(GroupTable.COLUMN_IMAGE, imageUri);  // Store image URI as a String
        values.put(GroupTable.COLUMN_CREATED_IN, new Date().toString());

        result = db.insert(GroupTable.TABLE_NAME, null, values);
        db.close();

        return (result != -1);
    }

    public boolean updateImage(int id, String imageUri){
        String where = GroupTable.COLUMN_ID + "=" + id;
        db = database.getWritableDatabase(); // Use getWritableDatabase()

        if (imageUri.isEmpty()) return false;

        ContentValues values = new ContentValues();
        values.put(GroupTable.COLUMN_IMAGE, imageUri);

        int result = db.update(GroupTable.TABLE_NAME, values, where, null);
        db.close();

        return result != -1;
    }

    public boolean updateName(int id, String name){
        String where = GroupTable.COLUMN_ID + "=" + id;
        db = database.getWritableDatabase(); // Use getWritableDatabase()

        if (name.isEmpty()) return false;

        ContentValues values = new ContentValues();
        values.put(GroupTable.COLUMN_NAME, name);

        int result = db.update(GroupTable.TABLE_NAME, values, where, null);
        db.close();

        return result != -1;
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
