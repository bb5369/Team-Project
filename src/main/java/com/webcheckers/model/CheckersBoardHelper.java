package com.webcheckers.model;

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

}
