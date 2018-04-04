package com.webcheckers.model;

/**
 * How to build a Checkers Board
 * This is a static class used in the setup of a CheckersGame
 */
public final class BoardBuilder {

    protected final static int WHITE_BORDER_INDEX = 2;
    protected final static int RED_BORDER_INDEX = 5;

    protected static int ROWS = 8;
    protected static int CELLS = 8;

    /**
     * buildRow builds a single row of a board
     * The first row of the board starts with a black square
     *
     * @param rowId - the row being built
     * @return - Space[] representing a row of Spaces
     */
    private static Space[] buildRow(int rowId) {

        Space[] row = new Space[CELLS];

        // Should this row start with a black space?
        boolean startOnBlack = (rowId % 2 == 0);

        boolean cellValid = startOnBlack;

        for (int cellId = 0; cellId < CELLS; cellId++) {
            row[cellId] = buildSpace(rowId, cellId, cellValid);

            cellValid = !cellValid; // alternate
        }

        return row;
    }

    /**
     * Build a Space given a row and cell ID context
     *
     * @param rowId        - ID of the row being built
     * @param cellId       - ID of the cell being built
     * @param invalidSpace - Whether or not a space is invalid
     * @return - A valid space
     */
    private static Space buildSpace(int rowId, int cellId, boolean invalidSpace) {

        if (invalidSpace) {
            return new Space(cellId, Space.State.INVALID);
        } else {

            if (rowId <= WHITE_BORDER_INDEX) {
                return new Space(cellId, new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));

            } else if (rowId >= RED_BORDER_INDEX) {
                return new Space(cellId, new Piece(Piece.Type.SINGLE, Piece.Color.RED));

            } else {
                return new Space(cellId, Space.State.OPEN);

            }
        }
    }

    /**
     * Build a board according to the parameters we have set in this object
     * <p>
     * We build it from the top down. Checkers rules says that there must be a black space
     * in the corner. So startRowBlackSquare = true;
     *
     * @return - Default checkers board matrix
     */
    public static Space[][] buildBoard() {

        Space[][] board = new Space[ROWS][CELLS];

        for (int rowId = 0; rowId < ROWS; rowId++) {
            board[rowId] = buildRow(rowId);
        }

        return board;
    }
}
