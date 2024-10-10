package com.srbenicio.slist.creators;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseCreator extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME="SList.db";
    private static final int DATABASE_VERSION=1;

    public DatabaseCreator(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GroupTable.CREATE_TABLE);
        db.execSQL(ItemTypeTable.CREATE_TABLE);
        db.execSQL(ItemTable.CREATE_TABLE);
        ItemTypeTable.insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + GroupTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemTypeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemTable.TABLE_NAME);
        onCreate(db);
    }

}
