package com.webcheckers.model;


public class Message {
    public enum MessageType{info, error}

    private MessageType type;
    private String text;

    public Message(String text, MessageType type)
    {
        this.text = text;
        this.type = type;
    }

    public String getText()
    {
        return text;
    }

    public MessageType getType()
    {
        return type;
    }
}
