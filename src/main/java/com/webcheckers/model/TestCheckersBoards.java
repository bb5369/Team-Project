package com.webcheckers.model;

public final class TestCheckersBoards {

	/*
	We define pawn in this case to mean 'piece that will be moved'
	Get your chess outta here.
	 */
	public static final int RED_PAWN_ROW = 7;
	public static final int RED_PAWN_CELL = 0;
	public static final Position RED_PAWN_POSITION = new Position(RED_PAWN_ROW, RED_PAWN_CELL);

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
					RED_PAWN_POSITION)
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

	/**
	 * For this board I did something special. I realized that we had too many magic numbers
	 * so I created some constants, and a constant red piece that tests can use to start their move
	 * @return CheckersBoardBuilder
	 */
	public static CheckersBoardBuilder singleJumpToEnd() {
		return CheckersBoardBuilder.aBoard()
				.withPieceAt(
						new Piece(Piece.Type.SINGLE, Piece.Color.WHITE),
						new Position(RED_PAWN_ROW - 1, RED_PAWN_CELL + 1)
				)
				.withPieceAt(
						new Piece(Piece.Type.SINGLE, Piece.Color.RED),
						RED_PAWN_POSITION
				);
	}
}
