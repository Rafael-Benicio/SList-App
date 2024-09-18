package com.srbenicio.slist.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.srbenicio.slist.DatabaseCreator;

import java.util.Date;

public class DatabaseGroupController {
	private SQLiteDatabase db;
    private final DatabaseCreator database;

    ContentValues values;
    long result;

    public DatabaseGroupController(Context context){
        database = new DatabaseCreator(context);
    }

    public String insert(String name, String image){
        db = database.getWritableDatabase();

        values = new ContentValues();
        values.put(DatabaseCreator.IG_NAME, name);
        values.put(DatabaseCreator.IG_IMAGE, image);
        values.put(DatabaseCreator.IG_CREATED_IN, new Date().toString());

        result = db.insert(DatabaseCreator.TABLE_IG, null, values);
        db.close();

        return  (result != -1)? "A group was created" : "Fail in create a new group" ;
    }

    public Cursor loadData(){
        Cursor cursor;
        String[] fields = {DatabaseCreator.IG_ID, DatabaseCreator.IG_NAME};

        db = database.getReadableDatabase();
        cursor=db.query(DatabaseCreator.TABLE_IG, fields, null,null,null,null,null,null);

        if (cursor!=null){
            cursor.moveToFirst();
        }

        db.close();

        return  cursor;
    }

    public boolean delete(int id){
        String where = DatabaseCreator.IG_ID + "=" + id;
        db = database.getReadableDatabase();
        int result=db.delete(DatabaseCreator.TABLE_IG,where,null);
        db.close();

        return result != -1;
    }

}
