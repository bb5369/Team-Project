package com.webcheckers.model;

public class Position {

    //instance variables
    private int row;
    private int cell;

    /**
     * Parameterized constructor
     * pass in the row and cell to the constructor
     *
     * @param row  - row ID of the cell
     * @param cell - space ID within the row
     */
    Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    /**
     * This method returns the x coordinate of the position
     *
     * @return - row index
     */
    public int getRow() {
        return this.row;
    }

    /**
     * This absolute difference of the end and start positions
     *
     * @param end   - final position of the move
     * @param start - starting position of the move
     * @return position with abosolute difference of the start and end positions
     */
    public static Position absoluteDifference(Position end, Position start) {
        int x = Math.abs(end.row - start.row);
        int y = Math.abs(end.cell - start.cell);

        return new Position(x, y);
    }

    /**
     * Determines whether or not the position is on the board
     *
     * @return - true if the end position of the move is on the board, false otherwise
     */
    public boolean isOnBoard(){
    	return (row >= 0 && row < CheckersBoardBuilder.ROWS) &&
                (cell >= 0 && cell < CheckersBoardBuilder.CELLS);
    }

    /**
     * This method returns the y coordinate of the position
     *
     * @return - y coordinate of the position
     */
    public int getCell() {
        return this.cell;
    }

    /**
     * This method generates a string representing the state of the
     * Position object
     *
     * @return - state of the object in the a string
     */
    public String toString() {
        return new String("RowGen: " + row + " Column: " + cell);
    }

}
