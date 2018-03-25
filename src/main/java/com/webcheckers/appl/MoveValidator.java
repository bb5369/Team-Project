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

	private enum SpaceState {
		INVALID,
		OPEN,
		WHITE_OCCUPIED,
		RED_OCCUPIED
	}

	public boolean validateMove(CheckersGame game, Player player, Move move) {
		LOG.fine(String.format("Validating move for Player [%s]", player.getName()));

		// Get matrix view of board
		SpaceState[][] matrix = buildBoardMatrix(game.getBoard());

		// Trace log our board matrix
		for (SpaceState[] row : matrix) {
			LOG.finest(Arrays.toString(row));
		}

		// Make sure starting position is accurate (player is moving a piece they own)
		Piece.color playerColor = game.getPlayerColor(player);
		int startX = move.getStartCell();
		int startY = move.getStartRow();

		SpaceState startState = matrix[startY][startX];

		if (playerColor == Piece.color.RED && startState != SpaceState.RED_OCCUPIED) {
			return false;
		}

		if (playerColor == Piece.color.WHITE && startState != SpaceState.WHITE_OCCUPIED) {
			return false;
		}


		// Now lets check the target space
		int endX = move.getEndCell();
		int endY = move.getEndRow();


		// Check the vector, it should be diagonal
		boolean isDiagonalMove = isDiagonalMove(move);
		boolean isValid = isDiagonalMove;
		SpaceState endState = matrix[endY][endX];

		int xDiff = Math.abs(endX- startX);
		int yDiff = Math.abs(endY - startY);
		if(xDiff > 1 || yDiff >1)
		{
			isValid = isValid && isValidJumpMove(move);
		}

		return (isValid && endState == SpaceState.OPEN);
	}

	boolean isDiagonalMove(Move move) {
		int deltaY = Math.abs(move.getStartRow() - move.getEndRow());
		int deltaX = Math.abs(move.getStartCell() - move.getEndCell());

		return (deltaY == deltaX);

	}

	/**
	 * :TODO implement is a valid jump move, For now return false for test
	 * purposes assuming no valid jump move will me made
	 * @return
	 */
	boolean isValidJumpMove(Move move)
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
