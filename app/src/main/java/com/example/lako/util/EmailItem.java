package com.example.lako.util;


public class EmailItem {
    private String email;
    private boolean isVerified;

    public EmailItem() {
        // Default constructor required for Firebase
    }

    public EmailItem(String email, boolean isVerified) {
        this.email = email;
        this.isVerified = isVerified;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}


