package com.webcheckers.model;

import java.util.StringJoiner;

/**
 * Builder pattern that generates a checkers board
 * We use the static component `BoardBuilder` to generate the default starting board
 * and then use method chaining to mutate the board state to create specific test cases.
 *
 * Not to be confused with `BoardBuilderTest` which is what tests the real component.
 * This Builder is only used in the model test tier to setup a non-starting game state.
 */
public class TestBoardBuilder {

	private Space[][] board;

	/**
	 * Seed our new object with a board (should be empty, but is not right now)
	 */
	private TestBoardBuilder() {
		board = BoardBuilder.buildBoard();
	}

	/**
	 * The builder starts with an empty board
	 */

	public static TestBoardBuilder aBoard() {
		return new TestBoardBuilder();
	}

	/**
	 * Add a given piece at the given position
	 *
	 * @param piece
	 * @param pos
	 * @return
	 */
	public TestBoardBuilder withPieceAt(Piece piece, Position pos) {
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
	public TestBoardBuilder withoutPieceAt(Position pos) {
		Space target = board[pos.getRow()][pos.getCell()];

		target.removePiece();

		return this;
	}

	/**
	 * Fluent interface method
	 * @return
	 */
	public TestBoardBuilder but() {
		return this;
	}

	/**
	 * Actually get the usable board that we've constructed
	 * @return Space[][] - constructed board
	 */
	public Space[][] build() {
		return board;
	}

	/**
	 * A starting board looks like this.
	 * Where [.] = invalid space and [_] = open space

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
	public TestBoardBuilder logBoard() {

		System.out.println("Board as it has been constructed:");

		StringJoiner row;

		for (Space[] r : board) {

			row = new StringJoiner(" ");

			for (Space c : r) {
				if (c.isOpen()) {

					row.add("_");
				}

				if (!c.isValid()) {
					row.add(".");
				}

				switch (c.getPiece().getColor()) {
					case WHITE:
						row.add("W");
						break;
					case RED:
						row.add("R");
						break;
				}
			}

			System.out.println(row.toString());
		}

		System.out.println("End of board view");

		return this;
	}
}
