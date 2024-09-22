package com.srbenicio.slist;

public class Item {
    private int id;
    private String title;
    private int imageResource;

    public Item(int id,String title, int imageResource) {
        this.id = id;
        this.title = title;
        this.imageResource = imageResource;
    }

    // Getters
    public String getTitle() { return title; }
    public int getImageResource() { return imageResource; }
}
