package com.webcheckers.model;

/**
 * A move is identified by a start and end position, and a piece color
 */
public class Move {

    //instance variable
    private Position start;
    private Position end;

    private Player player;
    private Piece.Color pieceColor;

    /**
     * param constructor take in the start and end position
     *
     * @param start - start position of the move
     * @param end   - end position of the move
     */
    Move(Position start, Position end) {
        this.start = start;
        this.end = end;
        player = null;
        pieceColor = null;
    }

    /**
     * constructor used when building a Move in tests and prediction
     * @param start
     * @param end
     * @param player
     * @param color
     */
    Move(Position start, Position end, Player player, Piece.Color color) {
        this.start = start;
        this.end = end;
        this.player = player;
        this.pieceColor = color;
    }

    /**
     * Used to access start position of the move
     *
     * @return - start position of the move
     */
    public Position getStart() {
        return this.start;
    }

    /**
     * Used to access end position of the move
     *
     * @return - end position of the move
     */
    public Position getEnd() {
        return this.end;
    }

    /**
     * Returns the middle Position between the start and end in this move
     * If the move is a single space, we return the start space.
     * @return Position - midpoint between start and end
     */
    public Position getMidpoint() {
    	if (!isSingleSpace()) {
            int midRow = getStartRow() + (getEndRow() - getStartRow()) / 2;
            int midCell = getStartCell() + (getEndCell() - getStartCell()) / 2;

            return new Position(midRow, midCell);
        } else {
            return start;
        }
    }

    /**
     * Used to access the x coordinate of the start position
     *
     * @return - x coordinate of the start position
     */
    public int getStartRow() {
        return start.getRow();
    }

    /**
     * Used to access the y coordinate of the start position
     *
     * @return - the cell
     */
    public int getStartCell() {
        return start.getCell();
    }


    /**
     * Used to access the x coordinate of the end position
     *
     * @return - x coordinate of the end position
     */
    public int getEndRow() {
        return end.getRow();
    }

    /**
     * Used to access the y coordinate of the end position
     *
     * @return - y coordinate of the end position
     */
    public int getEndCell() {
        return end.getCell();
    }


    /**
     * Get the color of the piece we are moving
     * @return
     */
    public Piece.Color getPieceColor() {
        return pieceColor;
    }

    /**
     * Get the player name who is making the move
     * @return
     */
    public String getPlayerName() {
        return player.getName();
    }

    /**
     * Set the player making the move
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Set the piece color being moved
     * @param color
     */
    public void setPieceColor(Piece.Color color) {
        this.pieceColor = color;
    }

    /**
     * Is the move to and from valid places on the checkers board?
     * Is the move a single space, or jump move?
     * Is the move diagonal?
     *
     * @return boolean
     */
    public boolean isValid() {
        return (start.isOnBoard() && end.isOnBoard()
                && this.getPieceColor() != null && this.player != null);
    }

    /**
     * Determines if a jump move was attempted
     *
     * @return - true if the jump move was attempted
     */
    public boolean isJump() {
        Position diff = Position.absoluteDifference(end, start);
        return diff.getCell() == 2 && diff.getRow() == 2;
    }

    /**
     * Checks to see if we are only moving one space away
     *
     * @return boolean - true if the move is moving one space away from start position, false otherwise
     */
    public boolean isSingleSpace() {
        int deltaY = Math.abs(start.getRow() - getEndRow());
        int deltaX = Math.abs(start.getCell() - end.getCell());

        return (deltaY == 1 && deltaX == 1);

    }

	/**
     * All moves must be diagonal, therefore rise==run
     *
     * @return boolean - true if the move being made is diagonal from the starting position

    public boolean isDiagonal() {
        int deltaY = Math.abs(start.getRow() - end.getRow());
        int deltaX = Math.abs(getStartCell() - getEndCell());

        return (deltaY == deltaX);
    }*/

    /**
     * This generates a string representing the state of the Move object
     *
     * @return - string representing the state of the move object
     */
    public String toString() {
    	int startRow, startCell, endRow, endCell;

        startCell = start.getCell();
        startRow = start.getRow();

        endCell = end.getCell();
        endRow = end.getRow();

        return String.format("<%d,%d> to <%d,%d>",
                startRow, startCell,
                endRow, endCell);
    }
}
