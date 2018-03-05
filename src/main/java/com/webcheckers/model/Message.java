package com.webcheckers.model;


public class Message {

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

    public String getType()
    {
        // Todo: this is unclean, so it should be fixed. Css selectors are case sensitive
        return this.getTypeStringLower();
    }

    private String getTypeStringLower() {
        return type.name().toLowerCase();
    }
}
