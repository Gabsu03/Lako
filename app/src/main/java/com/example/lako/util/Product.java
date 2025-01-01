package com.example.lako.util;

import android.net.Uri;

public class Product {
    private String id;  // Add the ID field
    private String name;
    private String price;
    private String image;
    private String description;
    private String specification;

    // Default constructor
    public Product() {
    }

    // Constructor including the ID, name, price, image, description, and specification
    public Product(String id, String name, String price, String image, String description, String specification) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.specification = specification;
    }

    // Getter and setter for ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters and setters for other fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }
}



