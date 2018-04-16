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
							if (c.getPiece().getType() == KING)
								row.add("I");
							else
								row.add("W");
							break;
						case RED:
							if (c.getPiece().getType() == KING)
								row.add("K");
							else
								row.add("R");
							break;
					}
				}
			}
			boardStringJoiner.add(row.toString());
		}

		return boardStringJoiner.toString();
	}

}
