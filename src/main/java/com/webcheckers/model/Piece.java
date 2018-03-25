package com.webcheckers.model;

/**
 * <p>Title: Piece class</p>
 * <p>Description: This class represents a checkers piece</p>
 */
public class  Piece {

    //enums
    public enum type {SINGLE, KING}
    public enum color {RED, WHITE}

    //instance
    private type ty;
    private color col;

    /**
     * Parameterized constructor
     * This initializes the type and color
     * to the passed in values
     * @param ty - type of the piece (SINGLE, KING)
     * @param col - Color of the PIECE (RED, WHITE)
     */
    public Piece(type ty, color col){
        this.ty = ty;
        this.col = col;
    }

    /**
     * getType method
     * getter for type
     * @return ty(types) value
     */
    public type getType(){
        return ty;
    }

    /**
     * getColor method
     * getter for col (color)
     * @return color
     */
    public color getColor(){
        return col;
    }

    public String toString()
    {
        return new String("Piece: "
                + ((col==color.RED)? "Red " : "White ")
                + ((ty == type.SINGLE)? "Single" : "King"));
    }

}
