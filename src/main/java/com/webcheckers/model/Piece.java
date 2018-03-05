package com.webcheckers.model;

/**
 * @author Alexis Halbur
 */
public class Piece {

    private enum type {SINGLE, KING}
    private enum color {RED, WHITE}

    private type ty;
    private color col;

    public Piece(type ty, color col){
        this.ty = ty;
        this.col = col;
    }

    public type getType(){
        return ty;
    }

    public color getColor(){
        return col;
    }

}
