package com.example.lako.util;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String productId;
    private String orderId;
    private String userId;
    private String name;
    private String image;
    private String price;
    private int quantity;
    private String sellerId;
    private String sellerName;
    private String shopName;

    // Default constructor for Firebase
    public CartItem() {}

    // Constructor with all fields
    public CartItem(String productId, String orderId, String name, String image, String price, int quantity, String sellerId, String sellerName, String shopName, String userId) {
        this.productId = productId;
        this.orderId = orderId;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.shopName = shopName;
        this.userId = userId;
    }

    // Parcelable implementation
    protected CartItem(Parcel in) {
        productId = in.readString();
        orderId = in.readString();
        userId = in.readString();
        name = in.readString();
        image = in.readString();
        price = in.readString();
        quantity = in.readInt();
        sellerId = in.readString();
        sellerName = in.readString();
        shopName = in.readString();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(orderId);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(price);
        dest.writeInt(quantity);
        dest.writeString(sellerId);
        dest.writeString(sellerName);
        dest.writeString(shopName);
    }
}
