package com.example.lako.util;

public class Product {

    private String name;
    private String description;
    private String price;
    private String specification;
    private String tags;
    private String imageUri;

    public Product(String name, String description, String price, String specification, String tags, String imageUri) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.specification = specification;
        this.tags = tags;
        this.imageUri = imageUri;
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

    public String getImageUri() {
        return imageUri;
    }
}


