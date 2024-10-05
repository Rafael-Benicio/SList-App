package com.srbenicio.slist.creators;

public class GroupTable {

    public static final String TABLE_NAME = "itens_group";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_CREATED_IN = "created_in";

    // SQL to create the group table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_IMAGE + " TEXT, " +
            COLUMN_CREATED_IN + " TIMESTAMP );";
}
