package com.srbenicio.slist.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.srbenicio.slist.DatabaseCreator;

import java.util.Date;

public class DatabaseItemController {
    private SQLiteDatabase db;
    private final DatabaseCreator database;

    ContentValues values;
    long result;

    public DatabaseItemController(Context context){
        database = new DatabaseCreator(context);
    }

    public boolean insert(String name, String desc, int type_id, int group_id){
        db = database.getWritableDatabase();

        values = new ContentValues();

        values.put(DatabaseCreator.I_GROUP_ID, group_id );
        values.put(DatabaseCreator.I_TYPE_ID, type_id );

        values.put(DatabaseCreator.I_NAME, name );
        values.put(DatabaseCreator.I_DESC, desc );

        values.put(DatabaseCreator.I_CREATED_IN, new Date().toString());
        values.put(DatabaseCreator.I_LAST_UPDATE, new Date().toString());

        result = db.insert(DatabaseCreator.TABLE_I, null, values);
        db.close();

        return  result != -1;
    }

    public boolean delete(int id){
        String where = DatabaseCreator.I_ID + "=" + id;
        db = database.getReadableDatabase();
        int result=db.delete(DatabaseCreator.TABLE_I,where,null);
        db.close();

        return result != -1;
    }

    public boolean updateRecord(int id, int record){
        String where = DatabaseCreator.I_ID + "=" + id;
        db = database.getReadableDatabase();

        values = new ContentValues();
        values.put(DatabaseCreator.I_RECORD, record);
        values.put(DatabaseCreator.I_LAST_UPDATE, new Date().toString());

        db.update(DatabaseCreator.TABLE_I,values,where,null);
        db.close();

        return result != -1;
    }


    public Cursor loadData(int group_id){
        Cursor cursor;
        String[] fields = {
                DatabaseCreator.I_ID,
                DatabaseCreator.I_GROUP_ID,
                DatabaseCreator.I_TYPE_ID,
                DatabaseCreator.I_RECORD,
                DatabaseCreator.I_NAME,
                DatabaseCreator.I_DESC,
                DatabaseCreator.I_CREATED_IN,
                DatabaseCreator.I_LAST_UPDATE
        };

        db = database.getReadableDatabase();
        cursor=db.query(DatabaseCreator.TABLE_I, fields, DatabaseCreator.I_GROUP_ID+"="+group_id,null,null,null,null,null);

        if (cursor!=null){
            cursor.moveToFirst();
        }

        db.close();

        return  cursor;
    }
}
