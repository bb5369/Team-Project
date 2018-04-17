package com.webcheckers.model;

import java.util.StringJoiner;

import static com.webcheckers.model.Piece.Type.KING;

public class CheckersBoardHelper {

	/**
	 * Return the Space on the board represented by the position
	 * @param board
	 * @param position
	 * @return Space
	 */
	public static Space getSpace(Space[][] board, Position position) {
		return board[position.getRow()][position.getCell()];
	}

	/**
	 * Returns a string format of the given board matrix
	 *
	 * A starting board looks like this.
	 * Where
	 *      [.] = invalid space
	 *      [_] = open space
	 *      [W] = White Single Piece
	 *      [I] = White King Piece
	 *
	 *      [R] = Red Single Piece
	 *      [K] = Red King Piece

	   0 1 2 3 4 5 6 7
	 0  . W . W . W . W
	 1  W . W . W . W .
	 2  . W . W . W . W
	 3  _ . _ . _ . _ .
	 4  . _ . _ . _ . _
	 5  R . R . R . R .
	 6  . R . R . R . R
	 7  R . R . R . R .

	 * @return
	 */
	public static String formatBoardString(Space[][] board) {

		StringJoiner rowString;
		StringJoiner boardStringJoiner = new StringJoiner("\n");

		boardStringJoiner.add("BOARD");
		boardStringJoiner.add("  0 1 2 3 4 5 6 7");

		for (int row=0; row < CheckersBoardBuilder.ROWS; row++) {

			Space[] r = board[row];
			rowString = new StringJoiner(" ");
			rowString.add(row + ""); // m4d l33t h4x

			for (Space c : r) {
				if (c.isOpen()) {
					rowString.add("_");
					continue;
				}

				if (c.getState() == Space.State.INVALID) {
					rowString.add(".");
				} else {
					switch (c.getPiece().getColor()) {
						case WHITE:
							if (c.getPiece().getType() == KING)
								rowString.add("I");
							else
								rowString.add("W");
							break;
						case RED:
							if (c.getPiece().getType() == KING)
								rowString.add("K");
							else
								rowString.add("R");
							break;
					}
				}
			}
			boardStringJoiner.add(rowString.toString());
		}

		return boardStringJoiner.toString();
	}

}
