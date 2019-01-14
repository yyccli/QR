package com.example.hasee.express.express.beans;

public class ListItemMessage {
    private String receiver;
    private String sender;

    public ListItemMessage(String receiver, String sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
