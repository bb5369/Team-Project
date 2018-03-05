package com.webcheckers.model;

/**
 * @author Alexis Halbur
 */
public class Space {

    private int cellIdx;
    private Piece currPiece;

    public Space(int cellIdx){
        this.cellIdx = cellIdx;
        this.currPiece = null;
    }

    public Space(int cellIdx, Piece currPiece){
        this.cellIdx = cellIdx;
        this.currPiece = currPiece;
    }

    public int getCellIdx(){
        return this.cellIdx;
    }

    public boolean isValid(){
        return true;
    }

    public Piece getPiece(){
        return null;
    }

}
