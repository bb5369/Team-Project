package com.webcheckers.model;

import java.util.Scanner;

/**
 * <p>Title: Space class</p>
 * <p>Description: This class represents a single space on a checkers board</p>
 */
public class Space{

    public enum State {
        INVALID,
        OPEN,
        OCCUPIED
    }

    //instance variables
    private int cellIdx;
    private Piece currPiece;
    private State state;


    /**
     * Parameterized constructor
     * This intializes cellIdx and currPiece to
     * the the value passed as a variable
     *
     * @param cellIdx   - index within the row of the space
     * @param currPiece - piece on the space
     */
    public Space(int cellIdx, Piece currPiece) {
        this.cellIdx = cellIdx;
        if (currPiece != null) {
            this.currPiece = currPiece;
            this.state = State.OCCUPIED;
        } else {
            this.currPiece = null;
            this.state = State.OPEN;
        }
    }

    /**
     * Custom space constructor
     * Used in practice to build a space without a piece
     *
     * @param cellIdx - index within the row of the space
     * @param state   - state of the space
     */
    public Space(int cellIdx, State state) {
        this.cellIdx = cellIdx;
        this.currPiece = null;
        this.state = state;
    }

    /**
     * This method returns a cell index
     *
     * @return - cell index
     */
    public int getCellIdx() {
        return this.cellIdx;
    }

    /**
     * to access the state of the space
     *
     * @return - state enum inside the space
     */
    public State getState() {
        return this.state;
    }

    /**
     * @param source - starting space that is being moved from
     * @return - returns true if source is not null, is occupied, or has no piece
     */
    public boolean movePieceFrom(Space source) {
        if (source == null) {
            return false;
        }

        if (state != State.OPEN) {
            return false;
        }

        if (source.getPiece() == null) {
            return false;
        }

        addPiece(source.getPiece());
        source.removePiece();

        return true;
    }

    /**
     *
     * @param source - starting space that is being moved from
     * @param jumped - space inbetween the start and end of the jump
     * @return returns true is not null, isoccupied or has not piece
     */
    public boolean jumpPieceMove(Space source, Space jumped){
        if (source == null) {
            return false;
        }

        if (state != State.OPEN || !jumped.isOccupied()) {
            return false;
        }

        if (source.getPiece() == null || jumped.getPiece() == null) {
            return false;
        }

        addPiece(source.getPiece());
        source.removePiece();
        jumped.removePiece();
        return true;
    }

    /**
     * Add a piece to this Space
     *
     * @param piece - piece being moved onto the space
     * @return - the changed state of the space
     */
    public State addPiece(Piece piece) {
        if (state == State.OPEN) {
            currPiece = piece;
            state = State.OCCUPIED;
            return state;

        } else {
            return state;
        }
    }


    /**
     * Removes the piece from the Space
     * and marks it as open
     *
     * @return - turns true if the space had a piece and was removed, false otherwise
     */
    public State removePiece() {
        if (state == State.OCCUPIED) {
            currPiece = null;
            state = State.OPEN;
            return state;
        } else {
            return state;
        }
    }

    /**
     * This method checks if the space is valid
     *
     * @return - true if space is valid, false otherwise
     */
    public boolean isValid() {
        return (state != State.INVALID && state != State.OCCUPIED);
    }

    /**
     * Determines whether the space is occupied or not
     *
     * @return - true if the space is occupied, false otherwise
     */
    public boolean isOccupied() {
        return this.state == State.OCCUPIED;
    }

    /**
     * Determines if the space is open or not
     *
     * @return - true if the space is open, false otherwise
     */
    public boolean isOpen() {
        return (this.state == State.OPEN);
    }

    /**
     * This method returns curPiece on the space
     *
     * @return - Piece occupying the space
     */
    public Piece getPiece() {
        return currPiece;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other == null && other instanceof Space)
            return false;

        if(this.cellIdx != ((Space)other).cellIdx)
            return false;
        if(!this.currPiece.equals(((Space) other).currPiece))
            return false;
        if(this.state != ((Space)other).state)
            return false;

        return true;
    }


    /**
     * clone method
     * This method return a cloned copy of the space object
     * Note: enums are apparently passed by reference
     * @return cloned copy of the Space object
     */
    public Space clone()
    {

        if(currPiece == null)
            return new Space(cellIdx, currPiece);

        return new Space(cellIdx, currPiece.clone());
    }



}
