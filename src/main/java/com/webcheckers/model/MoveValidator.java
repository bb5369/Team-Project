package com.webcheckers.model;

import java.util.logging.Logger;

/**
 * Move validator controller consumes a CheckersGame, Move, and Player
 * and determines if the Move is a valid one
 */
public class MoveValidator {

	private static final Logger LOG = Logger.getLogger(MoveValidator.class.getName());

	private CheckersGame game;
	private Player player;
	private Space[][] matrix;


	/**
	 * MoveValidator constructor for seeding info needed for the algorithm
	 * @param game
	 * @param player
	 */
	public MoveValidator(CheckersGame game, Player player) {

		this.game = game;
		this.player = player;

		matrix = game.getMatrix();

		LOG.fine(String.format("MoveValidator initialized for Player [%s]", player.getName()));
	}

	/**
	 * Entrypoint to move validation algorithm - kicks off the process
	 * @return
	 */
    public boolean validateMove(Move move) {

		LOG.fine(String.format("Validating move for Player [%s]", player.getName()));


		logBoardMatrix();
		logMoveCoordinates(move);
		logMoveStates(move);

		return validateMoveByStep(move);
	}

	/**
	 * Facade of move validation steps
	 * @return boolean if the given move is a valid one or not
	 */
	private boolean validateMoveByStep(Move move) {

		return 	isMoveDiagonal(move) &&
				(isMoveSingleSpace(move) || isMoveJump(move)) &&
				isMoveInRightDirection(move) &&
				isEndSpaceOpen(move);
	}

	/**
	 * Logs the matrix board for debug
	 */
	private void logBoardMatrix() {
        // Trace log our board matrix
		for (Space[] row : matrix) {
			StringBuilder rowStates = new StringBuilder();

		    for (Space space : row) {
		        rowStates.append(space.getState() + " ");
			}

			LOG.finest(rowStates.toString());
		}
	}

	/**
	 * Logs the move as a pair of coordinates
	 * @param move
	 */
	private void logMoveCoordinates(Move move) {
		// "RED Player [username] wants to move from <0,1> to <1,2>"
        LOG.finest(String.format("%s Player [%s] wants to move from %s",
                game.getPlayerColor(player),
                player.getName(),
                move.toString()));
	}

	/**
	 * Logs the current state of affairs at the two spaces involved in the move
	 */
	private void logMoveStates(Move move) {
        Space startSpace = getSpace(move.getStart());
		Space endSpace = getSpace(move.getEnd());

		LOG.finest(String.format("Starting position state is [%s]", startSpace.getState()));
		LOG.finest(String.format("End position state is [%s]", endSpace.getState()));

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
	 * Matrix lookup function - given a position it will return the enumerated state
	 * @param pos
	 * @return SpaceState
	 */
	private Space getSpace(Position pos) {
		return matrix[pos.getRow()][pos.getCell()];
	}
}
