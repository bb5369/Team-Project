package com.webcheckers.model;

public class Message {
    private enum type {info, error}
    public final type type;
    public final String message;

    public Message(String msg, type type)
    {
        message = msg;
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public type getType()
    {
        return type;
    }
}
