package com.webcheckers.model;

/**
 * <p>Title: Space class</p>
 * <p>Description: This class represents a single space on a checkers board</p>
 */
public class Space {

    public enum State {
		INVALID,
		OPEN,
		WHITE_OCCUPIED,
		RED_OCCUPIED
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
        this.currPiece = currPiece;

        switch (currPiece.getColor()) {
            case RED:
                this.state = State.RED_OCCUPIED;
                break;
            case WHITE:
                this.state = State.WHITE_OCCUPIED;
                break;
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

    public State getState() {
        return this.state;
    }

    /**
     * isValid method
     * This method checks if the space is valid
     * @return
     */
    public boolean isValid(){
		return (this.state != State.INVALID);
    }

    public boolean isOccupied() {
        return (this.state == State.WHITE_OCCUPIED || this.state == State.RED_OCCUPIED);
    }

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

    // TODO: Add setter for moving a piece onto a Space

}
