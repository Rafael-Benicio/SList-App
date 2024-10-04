package com.srbenicio.slist;

public class Item {
    private int id;
    private String title;
    private String imageUri;

    public Item(int id, String title, String imageUri) {
        this.id = id;
        this.title = title;
        this.imageUri = imageUri;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImageUri() { return imageUri; }
}
