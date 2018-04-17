package com.webcheckers.model;

public final class TestCheckersBoards {

	/**
	 * A checkers board where the RED player can jump over two white player pieces
	 * We place an additional white piece on the board so an end game is not triggered.
	 * For a board that ends in a win for RED use multiJumpToEnd
	 *
	 * . _ . W . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . W . _ . _
	 * _ . _ . _ . _ .
	 * . W . _ . _ . _
	 * R . _ . _ . _ .
	 *
	 * @return CheckersBoardBuilder
	 */
	public static CheckersBoardBuilder multiJump() {
		return CheckersBoardBuilder.aBoard()
			.withPieceAt(
					new Piece(Piece.Type.SINGLE, Piece.Color.WHITE),
					new Position(6, 1))
			.withPieceAt(
					new Piece (Piece.Type.SINGLE, Piece.Color.RED),
					new Position(7, 0))
			.withPieceAt(
					new Piece (Piece.Type.SINGLE, Piece.Color.WHITE),
					new Position(4, 3))
			.withPieceAt(
					new Piece(Piece.Type.SINGLE, Piece.Color.WHITE),
					new Position(0, 3));
	}

	/**
	 * Built off multiJump board, we place another RED piece on the board
	 * In the normal rules of checkers you must take the force jump and you should
	 * not be able to move the added RED piece
	 *
	 * . _ . W . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . W . _ . _
	 * _ . _ . _ . _ .
	 * . W . _ . _ . _
	 * R . _ . R . _ .
	 *
	 * @return CheckersBoardBuilder
	 */
	public static CheckersBoardBuilder forceAJump() {
		return multiJump()
				.withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.RED),
                        new Position(7, 4));
	}


	// ENDING BOARDS - one move will end the game

	/**
	 * Built from multiJump we remove the WHITE player's extra piece
	 * This allows the RED player to multi jump over the last two WHITE pieces
	 * and win the game.
	 *
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . W . _ . _
	 * _ . _ . _ . _ .
	 * . W . _ . _ . _
	 * R . _ . _ . _ .
	 *
	 * @return CheckersBoardBuilder
	 *
	 */
	public static CheckersBoardBuilder multiJumpToEnd() {
		return multiJump()
				.withoutPieceAt(new Position(0, 3));
	}
}
