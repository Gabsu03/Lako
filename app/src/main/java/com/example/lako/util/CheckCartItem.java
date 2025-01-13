package com.example.lako.util;

import android.os.Parcel;
import android.os.Parcelable;

public class CheckCartItem implements Parcelable {
    private String name;
    private String sellerID;
    private String sellerName;
    private String image;
    private int quantity; // New field for quantity
    private String price; // New field for price

    public CheckCartItem(String name, String sellerID, String sellerName, String image, int quantity, String price) {
        this.name = name;
        this.sellerID = sellerID;
        this.sellerName = sellerName;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    // Parcelable methods
    protected CheckCartItem(Parcel in) {
        name = in.readString();
        sellerID = in.readString();
        sellerName = in.readString();
        image = in.readString();
        quantity = in.readInt(); // Read quantity
        price = in.readString(); // Read price
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(sellerID);
        dest.writeString(sellerName);
        dest.writeString(image);
        dest.writeInt(quantity); // Write quantity
        dest.writeString(price); // Write price
    }

    public static final Creator<CheckCartItem> CREATOR = new Creator<CheckCartItem>() {
        @Override
        public CheckCartItem createFromParcel(Parcel in) {
            return new CheckCartItem(in);
        }

        @Override
        public CheckCartItem[] newArray(int size) {
            return new CheckCartItem[size];
        }
    };
}







