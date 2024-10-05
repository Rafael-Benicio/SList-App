package com.srbenicio.slist.creators;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class ItemTypeTable {

    public static final String TABLE_NAME = "itens_type";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";

    // SQL to create the item type table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TYPE + " TEXT );";

    // Insert initial data into the item type table
    public static void insertInitialData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, "list");
        db.insert(TABLE_NAME, null, values);
    }
}
