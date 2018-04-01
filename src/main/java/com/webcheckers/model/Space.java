package com.webcheckers.model;

/**
 * <p>Title: Space class</p>
 * <p>Description: This class represents a single space on a checkers board</p>
 */
public class Space {

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
     * @param cellIdx
     * @param currPiece
     */
    public Space(int cellIdx, Piece currPiece){
        this.cellIdx = cellIdx;
        if (currPiece != null) {
            this.currPiece = currPiece;
            this.state = State.OCCUPIED;
        } else {
            throw new NullPointerException("Piece constructor requires a non-null Piece");
        }
    }

    /**
     * Custom space constructor
     * Used in practice to build an open space
     * @param cellIdx
     * @param state
     */
    public Space(int cellIdx, State state) {
        this.cellIdx = cellIdx;
        this.currPiece = null;
        this.state = state;
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
     * getState method--
     * to access the state of the space
     * @return state enum inside the space
     */
    public State getState() {
        return this.state;
    }

    /**
     *
     * @param source
     * @return
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
     * Add a piece to this Space
     * @param piece
     * @return
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
     * removePiece method--
     * This method removes the piece from the Space
     * and marks it as open
     * @return turns true if the space had a piece and was removed, false otherwise
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
     * isValid method--
     * This method checks if the space is valid
     * @return
     */
    public boolean isValid(){
		return (state != State.INVALID);
    }

    /**
     * isOccupied method--
     * Determines whether the space is occupied or not
     * @return true if the space is occupied
     */
    public boolean isOccupied() {
        return this.state == State.OCCUPIED;
    }

    /**
     * isOpen method--
     * Determines if the space is open or not
     * @return true if the space is open, otherwise false
     */
    public boolean isOpen() {
        return (this.state == State.OPEN);
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
