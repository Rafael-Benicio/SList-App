package com.srbenicio.slist.creators;

public class ItemTable {

    public static final String TABLE_NAME = "item";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_TYPE_ID = "type_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RECORD = "record";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_CREATED_IN = "created_in";
    public static final String COLUMN_LAST_UPDATE = "last_update";

    // SQL to create the item table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_GROUP_ID + " INTEGER, " +
            COLUMN_TYPE_ID + " INTEGER, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_RECORD + " INTEGER, " +
            COLUMN_DESC + " TEXT, " +
            COLUMN_LAST_UPDATE + " TIMESTAMP, " +
            COLUMN_CREATED_IN + " TIMESTAMP, " +
            "FOREIGN KEY (" + COLUMN_GROUP_ID + ") REFERENCES " + GroupTable.TABLE_NAME + "(" + GroupTable.COLUMN_ID + "), " +
            "FOREIGN KEY (" + COLUMN_TYPE_ID + ") REFERENCES " + ItemTypeTable.TABLE_NAME + "(" + ItemTypeTable.COLUMN_ID + ") );";
}
