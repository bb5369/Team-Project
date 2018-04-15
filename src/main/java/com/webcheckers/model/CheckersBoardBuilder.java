package com.webcheckers.model;

import java.util.StringJoiner;
import java.util.logging.Logger;

import static com.webcheckers.model.Piece.Type.SINGLE;

/**
 * Builder pattern for generating checkers boards
 */
public class CheckersBoardBuilder {

    private final static Logger LOG = Logger.getLogger(CheckersBoardBuilder.class.getName());

    protected final static int WHITE_BORDER_INDEX = 2;
    protected final static int RED_BORDER_INDEX = 5;

    protected final static int ROWS = 8;
    protected final static int CELLS = 8;

    private Space[][] board;


    /**
     * Private constructor used in the named constructors aBoard, aStartingBoard
     */
    private CheckersBoardBuilder() {
        LOG.finest("Generated an empty checkers board");
        board = buildBoard();
    }

    /**
     * Returns a builder that starts with a board and red and white player pieces
     * @return
     */
    public CheckersBoardBuilder withStartingPieces() {
        addStartingPieces(board);

        return this;
    }

	/**
	 * The builder starts with an empty board
	 */
	public static CheckersBoardBuilder aBoard() {
	    LOG.finest("aBoard() - an empty board");

		return new CheckersBoardBuilder();
	}

    /**
     * Build a board with pieces in their starting positions
     */
    public static CheckersBoardBuilder aStartingBoard() {
        LOG.finest("aStartingBoard() - with red/white pieces in starting positions");

        return new CheckersBoardBuilder().withStartingPieces();
    }

    /**
     * Add a given piece at the given position
     *
     * @param piece
     * @param pos
     * @return
     */
    public CheckersBoardBuilder withPieceAt(Piece piece, Position pos) {
        LOG.finest("withPieceAt()");

        Space target = board[pos.getRow()][pos.getCell()];

        target.removePiece();
        target.addPiece(piece);

        return this;
    }

    /**
     * Remove a piece at the given position
     * @param pos
     * @return
     */
    public CheckersBoardBuilder withoutPieceAt(Position pos) {
        LOG.finest("withoutPieceAt()");

        Space target = board[pos.getRow()][pos.getCell()];

        target.removePiece();

        return this;
    }

    /**
     * Actually get the usable board that we've constructed
     * @return Space[][] - constructed board
     */
    public Space[][] getBoard() {
        return board;
    }


    /**
     * Returns the board currently under construction as a String
     * See the next method doc for example output
     * @return String
     */
	public String formatBoardString() {
	    return formatBoardString(board);
    }

    /**
     * Returns a string format of the given board matrix
     *
     * A starting board looks like this.
     * Where
     *      [.] = invalid space
     *      [_] = open space

     . W . W . W . W
     W . W . W . W .
     . W . W . W . W
     _ . _ . _ . _ .
     . _ . _ . _ . _
     R . R . R . R .
     . R . R . R . R
     R . R . R . R .

     * @return
     */
    public static String formatBoardString(Space[][] board) {

        StringJoiner row;
        StringJoiner boardStringJoiner = new StringJoiner("\n");

        boardStringJoiner.add("BOARD");

        for (Space[] r : board) {

            row = new StringJoiner(" ");

            for (Space c : r) {
                if (c.isOpen()) {
                    row.add("_");
                    continue;
                }

                if (c.getState() == Space.State.INVALID) {
                    row.add(".");
                } else {
                    switch (c.getPiece().getColor()) {
                        case WHITE:
                            row.add("W");
                            break;
                        case RED:
                            row.add("R");
                            break;
                    }
                }
            }
            boardStringJoiner.add(row.toString());
        }

        return boardStringJoiner.toString();
    }


    // PRIVATE METHODS

    /**
     * Build a board according to the parameters we have set in this object
     * <p>
     * We build it from the top down. Checkers rules says that there must be a black space
     * in the corner. So startRowBlackSquare = true;
     *
     * @return - Empty checkers board matrix
     */
    private static Space[][] buildBoard() {

        Space[][] board = new Space[ROWS][CELLS];

        for (int rowId = 0; rowId < ROWS; rowId++) {
            board[rowId] = buildRow(rowId);
        }

        return board;
    }

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
        boolean cellValid = (rowId % 2 != 0);


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
     * @param valid - Whether or not a space can contain a piece
     * @return - A valid space
     */
    private static Space buildSpace(int rowId, int cellId, boolean valid) {
        if (valid) {
            return new Space(cellId, Space.State.OPEN);
        } else {
            return new Space(cellId, Space.State.INVALID);
        }
    }


    /**
     * Add both red and white player's pieces to the board in starting positions
     * @param board
     */
    private static void addStartingPieces(Space[][] board) {
    	LOG.finest("addStartingPieces()");
        // TODO: Refactor

        // Add WHITE pieces
        for (int rowId = 0; rowId <= WHITE_BORDER_INDEX; rowId++) {
        	// Does this row start with a black space
            boolean cellValid = (rowId % 2 != 0);

            int startCell = (cellValid) ? 0 : 1;

            for (int cellId = startCell; cellId < CELLS; cellId += 2) {
                board[rowId][cellId].addPiece(new Piece(SINGLE, Piece.Color.WHITE));
            }
        }

        // Add RED pieces
        for (int rowId = RED_BORDER_INDEX; rowId < ROWS; rowId++) {
        	boolean cellValid = (rowId % 2 != 0);

        	int startCell = (cellValid) ? 0 : 1;

        	for (int cellId = startCell; cellId < CELLS; cellId += 2) {
        	    board[rowId][cellId].addPiece(new Piece(SINGLE, Piece.Color.RED));
            }
        }
    }

    /**
     * Clones the board so that moves can be tracked with board states
     * @param source: original board
     * @return: a clone of the original board
     */
    public static Space[][] cloneBoard(Space[][] source){

        Space[][] newBoard = new Space[ROWS][CELLS];

        for(int x = 0; x < ROWS; x++){
            for(int y = 0; y < CELLS; y++){
                newBoard[x][y] = source[x][y].clone();
            }
        }

        return newBoard;
    }

}
