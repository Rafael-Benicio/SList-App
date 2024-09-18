package com.srbenicio.slist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseCreator extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME="SList.db";
    private static final int DATABASE_VERSION=1;

//    GROUP_TABLE
    public static final String TABLE_IG ="itens_group";
    public static final String IG_ID ="id";
    public static final String IG_NAME ="name";
    public static final String IG_IMAGE ="image";
    public static final String IG_CREATED_IN ="created_in";

//    ITEM_TYPE_TABLE
    public static final String TABLE_IT ="itens_type";
    public static final String IT_ID ="id";
    public static final String IT_TYPE ="type";

//    ITEM_TABLE
    public static final String TABLE_I ="item";
    public static final String I_ID = "id";
    public static final String I_GROUP_ID = "group_id";
    public static final String I_TYPE_ID = "type_id";
    public static final String I_NAME = "name";
    public static final String I_RECORD = "record";
    public static final String I_DESC = "description";
    public static final String I_CREATED_IN = "created_in";
    public static final String I_LAST_UPDATE = "last_update";


    private static final String CREATE_GROUP_TABLE="CREATE TABLE "+
            TABLE_IG+ "( "+
            IG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            IG_NAME + " TEXT, "+
            IG_IMAGE + " TEXT, "+
            IG_CREATED_IN + " TIMESTAMP );";

    private static final String CREATE_TYPE_TABLE="CREATE TABLE "+
            TABLE_IT+ "( "+
            IT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            IT_TYPE + " TEXT );";

    private static final String CREATE_ITEM_TABLE="CREATE TABLE "+
            TABLE_I+ "( "+
            I_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            I_GROUP_ID + " INTEGER, "+
            I_TYPE_ID + " INTEGER, "+
            I_RECORD + " INTEGER, "+
            I_NAME + " TEXT, "+
            I_DESC + " TEXT, "+
            I_LAST_UPDATE + " TIMESTAMP, "+
            I_CREATED_IN + " TIMESTAMP, "+
            "FOREIGN KEY ("+I_GROUP_ID+") REFERENCES "+TABLE_IG+"("+IG_ID+"),"+
            "FOREIGN KEY ("+I_TYPE_ID+") REFERENCES "+TABLE_IT+"("+IT_ID+"));";

    public DatabaseCreator(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GROUP_TABLE);
        db.execSQL(CREATE_TYPE_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        typeTableConfigure(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_I);
        onCreate(db);
    }

    private void typeTableConfigure(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(DatabaseCreator.IT_TYPE, "list" );
        db.insert(DatabaseCreator.TABLE_IT, null, values);
    }

}
