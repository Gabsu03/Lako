package com.example.lako.util;

import android.net.Uri;

public class Product {
    private String name;
    private String price;
    private String image;
    private String description;  // Added description
    private String specification; // Added specification

    // Default constructor
    public Product() {
    }

    // Constructor including description and specification
    public Product(String name, String price, String image, String description, String specification) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.specification = specification;
    }

    // Getters and setters for name, price, image, description, and specification
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


