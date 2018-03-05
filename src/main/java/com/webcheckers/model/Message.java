package com.webcheckers.model;


public class Message {
<<<<<<< HEAD
    private enum type {INFO, ERROR}
    public final type type;
    public final String message;
=======
    private final MessageType type;
    private final String text;
>>>>>>> c2d793dc14c78dfa001d854f93459ce648301b51

    public Message(String text, MessageType type)
    {
        this.text = text;
        this.type = type;
    }

    public String getText()
    {
        return text;
    }

    public String getType()
    {
        // Todo: this is unclean, so it should be fixed. Css selectors are case sensitive
        return this.getTypeStringLower();
    }

    private String getTypeStringLower() {
        return type.name().toLowerCase();
    }
}
