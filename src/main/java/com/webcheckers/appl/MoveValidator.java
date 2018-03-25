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

	private SpaceState[][] matrix;

	private enum SpaceState {
		INVALID,
		OPEN,
		WHITE_OCCUPIED,
		RED_OCCUPIED
	}

	public boolean validateMove(CheckersGame game, Player player, Move move) {
		LOG.fine(String.format("Validating move for Player [%s]", player.getName()));

		// Get matrix view of board
		matrix = buildBoardMatrix(game.getBoard());

		// Trace log our board matrix
		for (SpaceState[] row : matrix) {
			LOG.finest(Arrays.toString(row));
		}

		// Make sure starting position is accurate (player is moving a piece they own)
		Piece.color playerColor = game.getPlayerColor(player);
		SpaceState startState = getPositionState(move.getStart());
		LOG.finest(String.format("Starting position is [%s]", startState));

		if (playerColor == Piece.color.RED && startState != SpaceState.RED_OCCUPIED) {
			return false;
		}

		if (playerColor == Piece.color.WHITE && startState != SpaceState.WHITE_OCCUPIED) {
			return false;
		}


		// Now lets check the target space
		SpaceState endState = getPositionState(move.getEnd());
		LOG.finest(String.format("Target position is [%s]", endState));

		// All moves are diagonal
		boolean isMoveDiagonal = isMoveDiagonal(move);

		// We can have single space moves, or jump moves
		// In this story we do not support jump moves
		boolean isMoveValidType = (isMoveSingleSpace(move) || isMoveJump(move));

		// The frontend code should prevent this, but server side checks are good
		boolean isTargetSpaceOpen = (endState == SpaceState.OPEN);

		// Unless a piece is king, a player can only move away from their starting row
		boolean isMoveForward = isMoveForward(playerColor, move);


		return (isMoveDiagonal && isMoveValidType && isTargetSpaceOpen && isMoveForward);
	}

	/**
	 * If a Piece is not a King then it can only move "forward"
	 * Forward is defined as "away from the player's side of the board"
	 * @param color
	 * @param move
	 * @return
	 */
	private boolean isMoveForward(Piece.color color, Move move) {
		// row for white player must increase
		// row for red player must decrease

		int startRow = move.getStartRow();
		int endRow = move.getEndRow();

		switch(color) {
			case RED:
				return (endRow < startRow); // moving up the board
			case WHITE:
				return (endRow > startRow); // moving down the board
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
	 * Because OO is sometimes hard we want our move algorithms to be able to directly translate cell,row coordinates
	 * into actionable space states, which is what this matrix provides.
	 * It gives a current view of the checkers board in a two-dimensional array of space state enums
	 * @param board
	 * @return
	 */
	private SpaceState[][] buildBoardMatrix(BoardView board) {
		SpaceState[][] matrix = new SpaceState[8][8];

		Iterator<Row> rowIterator = board.iterator();

		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int rowId = row.getIndex(); // Y

			Iterator<Space> spaceIterator = row.iterator();

			while (spaceIterator.hasNext()) {
				Space space = spaceIterator.next();
				int spaceId = space.getCellIdx(); // X

				matrix[rowId][spaceId] = getSpaceState(space);
			}
		}

		return matrix;
	}

	/**
	 * Matrix lookup function - given a position it will return the enumerated state
	 * @param pos
	 * @return SpaceState
	 */
	private SpaceState getPositionState(Position pos) {
		return matrix[pos.getRow()][pos.getCell()];
	}

	/**
	 * This method shouldn't exist since it smells like a proxy function
	 * The space model should have the state enum and we should directly pull it from there
	 * @param space
	 * @return
	 */
	private SpaceState getSpaceState(Space space) {
		if ( ! space.isValid()) {
			return SpaceState.INVALID;
		}

		Piece piece = space.getPiece();

		if (piece == null) {
			return SpaceState.OPEN;
		} else {
			switch (space.getPiece().getColor()) {
				case RED:
					return SpaceState.RED_OCCUPIED;

				case WHITE:
					return SpaceState.WHITE_OCCUPIED;
			}
		}

		return SpaceState.OPEN;
	}
}
