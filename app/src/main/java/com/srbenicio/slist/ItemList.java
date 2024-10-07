package com.srbenicio.slist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public void setRecord(int newRecord) { record=newRecord; }

    public String getCreatedIn() {
        return dataParser(createdIn);
    }

    public String getLastUpdate() { return dataParser(lastUpdate); }

    private String dataParser(String dateString){
        // Define the date format that matches the stored date string
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date;
        try {
            // Parse the stored date string into a Date object
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Return the original string if parsing fails
        }

        // Get the current date
        Date now = new Date();

        // Calculate the time difference in milliseconds
        long diffInMillis = now.getTime() - date.getTime();

        // Convert the time difference into days
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        if (diffInDays == 0) {
            return "today";
        } else if (diffInDays == 1) {
            return "yesterday";
        } else if (diffInDays >= 2 && diffInDays <= 6) {
            return diffInDays + " days ago";
        } else if (diffInDays >= 7 && diffInDays <= 29) {
            long weeks = diffInDays / 7;
            if (weeks == 1) {
                return "1 week ago";
            } else {
                return weeks + " weeks ago";
            }
        } else {
            long months = diffInDays / 30;
            if (months == 1) {
                return "1 month ago";
            } else {
                return months + " months ago";
            }
        }
    }

}
