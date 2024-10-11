package com.srbenicio.slist.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.srbenicio.slist.creators.DatabaseCreator;
import com.srbenicio.slist.creators.ItemTable;

import java.util.Date;

public class DatabaseItemController {
    private SQLiteDatabase db;
    private final DatabaseCreator database;

    ContentValues values;
    long result;

    public DatabaseItemController(Context context){
        database = DatabaseCreator.getInstance(context);
    }

    public boolean insert(String name, String desc, int type_id, int group_id){
        db = database.getWritableDatabase();

        values = new ContentValues();

        values.put(ItemTable.COLUMN_GROUP_ID, group_id );
        values.put(ItemTable.COLUMN_TYPE_ID, type_id );

        values.put(ItemTable.COLUMN_NAME, name );
        values.put(ItemTable.COLUMN_DESC, desc );

        values.put(ItemTable.COLUMN_CREATED_IN, new Date().toString());
        values.put(ItemTable.COLUMN_LAST_UPDATE, new Date().toString());

        result = db.insert(ItemTable.TABLE_NAME, null, values);
        db.close();

        return  result != -1;
    }

    public boolean delete(int id){
        String where = ItemTable.COLUMN_ID + "=" + id;
        db = database.getReadableDatabase();
        int result=db.delete(ItemTable.TABLE_NAME,where,null);
        db.close();

        return result != -1;
    }

    public boolean updateRecord(int id, int record){
        String where = ItemTable.COLUMN_ID + "=" + id;
        db = database.getWritableDatabase(); // Use getWritableDatabase()

        ContentValues values = new ContentValues();
        values.put(ItemTable.COLUMN_RECORD, record);
        values.put(ItemTable.COLUMN_LAST_UPDATE, new Date().toString());

        int result = db.update(ItemTable.TABLE_NAME, values, where, null);
        db.close();

        return result != -1;
    }


    public Cursor loadData(int group_id){
        Cursor cursor;
        String[] fields = {
                ItemTable.COLUMN_ID,
                ItemTable.COLUMN_GROUP_ID,
                ItemTable.COLUMN_TYPE_ID,
                ItemTable.COLUMN_RECORD,
                ItemTable.COLUMN_NAME,
                ItemTable.COLUMN_DESC,
                ItemTable.COLUMN_CREATED_IN,
                ItemTable.COLUMN_LAST_UPDATE
        };

        db = database.getReadableDatabase();
        cursor=db.query(ItemTable.TABLE_NAME, fields, ItemTable.COLUMN_GROUP_ID+"="+group_id,null,null,null,null,null);

        if (cursor!=null){
            cursor.moveToFirst();
        }

        db.close();

        return  cursor;
    }
}
