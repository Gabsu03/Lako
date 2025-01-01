package com.example.lako.models;

public class ChatRoom {
    private String shopName;
    private String lastMessage;
    private String chatId;
    private String username;
    private long timestamp;

    public ChatRoom() {}

    // Constructor with parameters
    public ChatRoom(String shopName, String lastMessage) {
        this.shopName = shopName;
        this.lastMessage = lastMessage;
    }

    // Getter for shopName
    public String getShopName() {
        return shopName;
    }

    // Setter for shopName
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    // Getter for lastMessage
    public String getLastMessage() {
        return lastMessage;
    }

    // Setter for lastMessage
    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    // New Getter for chatId
    public String getChatId() {
        return chatId;
    }

    // New Setter for chatId
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    // New Getter for username
    public String getUsername() {
        return username;
    }

    // New Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for timestamp
    public long getTimestamp() {
        return timestamp;
    }

    // Setter for timestamp
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
