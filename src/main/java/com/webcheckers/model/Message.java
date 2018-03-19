package com.webcheckers.model;


public class Message {
    //enum
    public enum MessageType{info, error}

    //instance variables
    private MessageType type;
    private String text;

    /**
     * parameterized constructor
     * intitializes all the text and type instance variables
     * using the passing in values
     * @param text - data for initializing text
     * @param type - data for initializing type
     */
    public Message(String text, MessageType type)
    {
        this.text = text;
        this.type = type;
    }

    /**
     * getText method
     * This method is a getter of text
     * @return reference to the text
     */
    public String getText()
    {
        return text;
    }

    /**
     * getType method
     * This method is a getter of type
     * @return enum type
     */
    public MessageType getType()
    {
        return type;
    }
}
