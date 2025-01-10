package com.example.lako.util;

public class CartItem {
    private String productId;
    private String name;
    private String image;
    private String price;
    private int quantity;
    private String sellerId;  // Added sellerId field

    public CartItem() { }

    public CartItem(String productId, String name, String image, String price, int quantity, String sellerId) {
        this.productId = productId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;  // Initialize sellerId
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSellerId() {  // Getter for sellerId
        return sellerId;
    }

    public void setSellerId(String sellerId) {  // Setter for sellerId
        this.sellerId = sellerId;
    }
}



