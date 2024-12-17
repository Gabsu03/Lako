package com.example.lako.util;

public class Product {
    private String id;
    private String name;
    private String description;
    private String price;
    private String specification;
    private String tags;
    private String imageUrl;

    // Default constructor required for Firebase
    public Product() {
    }

    public Product(String id, String name, String description, String price, String specification, String tags, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.specification = specification;
        this.tags = tags;
        this.imageUrl = imageUrl;
    }

    // Getters and setters (optional if not directly used)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getSpecification() {
        return specification;
    }

    public String getTags() {
        return tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

