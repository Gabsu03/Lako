package com.example.lako.models;

public class Message {
    private String messageText;
    private long timestamp;
    private String senderId;

    // Empty constructor for Firebase
    public Message() {}

    // Constructor for creating messages
    public Message(String messageText, long timestamp, String senderId) {
        this.messageText = messageText;
        this.timestamp = timestamp;
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSenderId() {
        return senderId;
    }
}
