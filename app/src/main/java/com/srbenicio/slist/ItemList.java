package com.srbenicio.slist;

public class ItemList {
    private int id;
    private String name;
    private String description;
    private int record;
    private String createdIn;
    private String lastUpdate;

    public ItemList(int id, String name, String description, int record, String createdIn, String lastUpdate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.record = record;
        this.createdIn = createdIn;
        this.lastUpdate = lastUpdate;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int newRecord) {
        record=newRecord;
    }

    public String getCreatedIn() {
        return createdIn;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
