package com.example.lako.util;

import android.os.Parcel;
import android.os.Parcelable;

public class SellerCartItem implements Parcelable {
    private String productId;
    private String productName;
    private String productImage;
    private String price;
    private int quantity;
    private String buyerId;
    private String sellerId; // Add this field
    private String status;
    private String orderId;
    private String addressLabel;
    private String addressName;
    private String addressPhone;
    private String fullAddress;

    public SellerCartItem() {}

    public SellerCartItem(String productId, String productName, String productImage, String price, int quantity,
                          String buyerId, String sellerId, String status, String orderId, String addressLabel,
                          String addressName, String addressPhone, String fullAddress) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
        this.quantity = quantity;
        this.buyerId = buyerId;
        this.sellerId = sellerId; // Initialize sellerId
        this.status = status;
        this.orderId = orderId;
        this.addressLabel = addressLabel;
        this.addressName = addressName;
        this.addressPhone = addressPhone;
        this.fullAddress = fullAddress;
    }

    // Parcelable implementation
    protected SellerCartItem(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        productImage = in.readString();
        price = in.readString();
        quantity = in.readInt();
        buyerId = in.readString();
        sellerId = in.readString(); // Add this line
        status = in.readString();
        orderId = in.readString();
        addressLabel = in.readString();
        addressName = in.readString();
        addressPhone = in.readString();
        fullAddress = in.readString();
    }

    public static final Creator<SellerCartItem> CREATOR = new Creator<SellerCartItem>() {
        @Override
        public SellerCartItem createFromParcel(Parcel in) {
            return new SellerCartItem(in);
        }

        @Override
        public SellerCartItem[] newArray(int size) {
            return new SellerCartItem[size];
        }
    };

    // Add getter and setter for sellerId
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAddressLabel() {
        return addressLabel;
    }

    public void setAddressLabel(String addressLabel) {
        this.addressLabel = addressLabel;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressPhone() {
        return addressPhone;
    }

    public void setAddressPhone(String addressPhone) {
        this.addressPhone = addressPhone;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeString(price);
        dest.writeInt(quantity);
        dest.writeString(buyerId);
        dest.writeString(sellerId); // Add this line
        dest.writeString(status);
        dest.writeString(orderId);
        dest.writeString(addressLabel);
        dest.writeString(addressName);
        dest.writeString(addressPhone);
        dest.writeString(fullAddress);
    }
}



