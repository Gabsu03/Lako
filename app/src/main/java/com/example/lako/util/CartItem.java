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

    // Getter for sellerId
    public String getSellerId() {
        return sellerId;
    }

    // Setter for sellerId
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    // Other getters and setters
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}





