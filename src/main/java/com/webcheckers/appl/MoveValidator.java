package com.webcheckers.appl;

import com.webcheckers.model.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Move validator controller consumes a CheckersGame, Move, and Player
 * and determines if the Move is a valid one
 */
public class MoveValidator {

	private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());

	private CheckersGame game;
	private Player player;
	private Move move;
	private Space[][] matrix;


	/**
	 * MoveValidator constructor for seeding info needed for the algorithm
	 * @param game
	 * @param player
	 * @param move
	 */
	public MoveValidator(CheckersGame game, Player player, Move move) {

		this.game = game;
		this.player = player;
		this.move = move;

		buildBoardMatrix();
	}

	/**
	 * Entrypoint to move validation algorithm - kicks off the process
	 * @return
	 */
    public boolean validateMove() {

		LOG.fine(String.format("Validating move for Player [%s]", player.getName()));

		logBoardMatrix();

		logMoveStates();

		return validateMoveByStep();
	}

	/**
	 * Facade of move validation steps
	 * @return boolean if the given move is a valid one or not
	 */
	private boolean validateMoveByStep() {

		return 	isMoveDiagonal(move) &&
				(isMoveSingleSpace(move) || isMoveJump(move)) &&
				isMoveInRightDirection(move) &&
				isEndSpaceOpen(move);
	}

	/**
	 * Helper function for logging the current state of start and end positions in a move
	 */
	private void logMoveStates() {
        Space startSpace = getSpace(move.getStart());
		Space endSpace = getSpace(move.getEnd());

		LOG.finest(String.format("Starting position is [%s]", startSpace.getState()));
		LOG.finest(String.format("End position is [%s]", endSpace.getState()));

	}

	private void logBoardMatrix() {
        // Trace log our board matrix
		for (Space[] row : matrix) {
			StringBuilder rowStates = new StringBuilder();

		    for (Space space : row) {
		        rowStates.append(space.getState());
			}

			LOG.finest(rowStates.toString());
		}
	}

	/**
	 * MOVE VALIDATION STEPS
	 */

	/**
	 * Given a move is the end position open
	 * @param move
	 * @return
	 */
	private boolean isEndSpaceOpen(Move move) {
		Space endSpace = getSpace(move.getEnd());

		return endSpace.isOpen();
	}

	/**
	 * Determines if the piece is moved in the right direction
	 * @param move
	 * @return True, if it does, false otherwise
	 */
	private boolean isMoveInRightDirection(Move move) {
	    Piece piece = getSpace(move.getStart()).getPiece();

		// If the piece is a king then they can move bi-directionally
		if (piece != null && piece.getType() == Piece.Type.KING) {
			return true;
		}

		// row for white player must increase
		// row for red player must decrease

		Piece.Color playerColor = game.getPlayerColor(player);
		int startRow = move.getStartRow();
		int endRow = move.getEndRow();

		switch (playerColor) {
			case RED:
				return (endRow < startRow);
			case WHITE:
				return (endRow > startRow);
		}

		return false;
	}

	/**
	 * Checks to see if we are only moving one space away
	 * @param move
	 * @return boolean
	 */
	private boolean isMoveSingleSpace(Move move) {
		int deltaY = Math.abs(move.getStartRow() - move.getEndRow());
		int deltaX = Math.abs(move.getStartCell() - move.getEndCell());

		LOG.finest(String.format("Move distance is %d rows and %d cells", deltaY, deltaX));

		return (deltaY == 1 && deltaX == 1);

	}

	/**
	 * All moves must be diagonal, therefore rise==run
	 * @param move
	 * @return boolean
	 */
	private boolean isMoveDiagonal(Move move) {
		int deltaY = Math.abs(move.getStartRow() - move.getEndRow());
		int deltaX = Math.abs(move.getStartCell() - move.getEndCell());

		return (deltaY == deltaX);

	}

	/**
	 * :TODO implement is a valid jump move, For now return false for test
	 * purposes assuming no valid jump move will me made
	 * @return
	 */
	private boolean isMoveJump(Move move)
	{
		return false;
	}



	/**
	 * BOARD MATRIX AND HELPERS
	 */

	/**
	 * Because OO is sometimes hard we want our move algorithms to be able to directly translate cell,row coordinates
	 * into actionable space states, which is what this matrix provides.
	 * It gives a current view of the checkers board in a two-dimensional array of space state enums
	 */
	private void buildBoardMatrix() {
		Space[][] matrix = new Space[8][8];

		Iterator<Row> rowIterator = this.game.getBoard().iterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int rowId = row.getIndex(); // Y

			Iterator<Space> spaceIterator = row.iterator();

			while (spaceIterator.hasNext()) {
				Space space = spaceIterator.next();
				int spaceId = space.getCellIdx(); // X

				matrix[rowId][spaceId] = space;
			}
		}

		this.matrix = matrix;
	}

	/**
	 * Matrix lookup function - given a position it will return the enumerated state
	 * @param pos
	 * @return SpaceState
	 */
	private Space getSpace(Position pos) {
		return matrix[pos.getRow()][pos.getCell()];
	}
}
