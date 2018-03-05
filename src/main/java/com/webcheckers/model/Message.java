package com.webcheckers.model;


public class Message {
    public final MessageType type;
    public final String message;

    public Message(String msg, MessageType type)
    {
        message = msg;
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public MessageType getType()
    {
        return type;
    }
}
