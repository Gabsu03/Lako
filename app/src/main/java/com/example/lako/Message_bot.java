package com.example.lako;

public class Message_bot {
    private String text;
    private boolean isUser;

    public Message_bot(String text, boolean isUser) {
        this.text = text;
        this.isUser = isUser;
    }

    public String getText() {
        return text;
    }

    public boolean isUser() {
        return isUser;
    }
}
