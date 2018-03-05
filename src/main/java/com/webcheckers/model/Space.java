package com.webcheckers.model;

/**
 * <p>Title: Space class</p>
 * <p>Description: This class represents a single space on a checkers board</p>
 */
public class Space {

    //instance variables
    private int cellIdx;
    private Piece currPiece;


    /**
     * parameterized constructor
     * This used to initialize a space by assigning it a index
     * @param cellIdx - cells index
     */
    public Space(int cellIdx){
        this.cellIdx = cellIdx;
        this.currPiece = null;
    }

    /**
     * Parameterized constructor
     * This intializes cellIdx and currPiece to
     * the the value passed as a variable
     * @param cellIdx
     * @param currPiece
     */
    public Space(int cellIdx, Piece currPiece){
        this.cellIdx = cellIdx;
        this.currPiece = currPiece;
    }

    /**
     * getCellIdx method
     * This method returns a cell index
     * @return cell index
     */
    public int getCellIdx(){
        return this.cellIdx;
    }

    /**
     * isValid method
     * This method checks if the space is valid
     * @return
     */
    public boolean isValid(){
        return true;
    }

    /**
     * getPiece method
     * This method returns curPiece on the space
     * @return
     */
    public Piece getPiece(){
        return currPiece;
    }

}
