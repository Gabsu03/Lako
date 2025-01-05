package com.example.lako.util;

public class Category {
    private String id;
    private String name;

    public Category() {
        // Default constructor required for Firebase
    }

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}




