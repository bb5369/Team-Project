package com.webcheckers.model;

/**
 * <p>Title: Piece class</p>
 * <p>Description: This class represents a checkers piece</p>
 */
public class  Piece {

    //enums
    public enum Type {SINGLE, KING}
    public enum Color {RED, WHITE}

    //instance
    private Type ty;
    private Color col;

    /**
     * Parameterized constructor
     * This initializes the type and color
     * to the passed in values
     * @param ty - type of the piece (SINGLE, KING)
     * @param col - Color of the PIECE (RED, WHITE)
     */
    public Piece(Type ty, Color col){
        this.ty = ty;
        this.col = col;
    }

    /**
     * getType method
     * getter for type
     * @return ty(types) value
     */
    public Type getType(){
        return ty;
    }

    /**
     * getColor method
     * getter for col (color)
     * @return color
     */
    public Color getColor(){
        return col;
    }

    public String toString()
    {
        return new String("Piece: "
                + ((col==Color.RED)? "Red " : "White ")
                + ((ty == Type.SINGLE)? "Single" : "King"));
    }

}
