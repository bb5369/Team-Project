package com.webcheckers.model;

/**
 * <p>Title: Piece class</p>
 * <p>Description: This class represents a checkers piece</p>
 */
public class Piece {

    //enums
    public enum Type {
        SINGLE, KING
    }

    public enum Color {RED, WHITE}

    //instance
    private Type type;
    private Color color;

    /**
     * Parameterized constructor
     * This initializes the type and color
     * to the passed in values
     *
     * @param type  - type of the piece (SINGLE, KING)
     * @param color - Color of the PIECE (RED, WHITE)
     */
    public Piece(Type type, Color color) {
        this.type = type;
        this.color = color;
    }

    /**
     * getter for type
     *
     * @return - type of the piece
     */
    public Type getType() {
        return type;
    }

    /**
     * getter for color
     *
     * @return - color of the piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * clone method
     * This method return a cloned copy of the object
     */
    public Piece clone()
    {
        return new Piece(this.type, this.color);
    }

    /**
     * Makes the piece a king
     */
    public void kingMe(){
        this.type = Type.KING;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == null && other instanceof Piece)
            return false;

        if(this.type != ((Piece)other).type)
            return false;
        if(this.color != ((Piece)other).color)
            return false;

        return true;
    }

    /**
     * Creates and returns a string detailing the piece's color and type
     *
     * @return - String created
     */
    public String toString() {
        return new String("Piece: "
                + ((color == Color.RED) ? "Red " : "White ")
                + ((type == Type.SINGLE) ? "Single" : "King"));
    }

}
