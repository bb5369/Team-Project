package com.webcheckers.model;

public final class TestCheckersBoards {

	public static final Piece RED_SINGLE_PIECE = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
	public static final Piece RED_KING_PIECE = new Piece(Piece.Type.KING, Piece.Color.RED);
	public static final Piece WHITE_SINGLE_PIECE = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);

	/*
	We define pawn in this case to mean 'piece that will be moved'
	Get your chess outta here.
	 */
	public static final int RED_PAWN_ROW = 7;
	public static final int RED_PAWN_CELL = 0;

	// The starting position of the RED piece moved in unit tests
	public static final Position RED_PAWN_POSITION = new Position(RED_PAWN_ROW, RED_PAWN_CELL);

	// The end Position of the first jump
	public static final Position RED_PAWN_JUMP_POSITION = new Position(
			RED_PAWN_ROW - 2,
			RED_PAWN_CELL + 2);

	// The end Position of the second jump
	public static final Position RED_PAWN_JUMP_SECOND_POSITION = new Position(
			RED_PAWN_ROW - 4,
			RED_PAWN_CELL + 4);

	// This is the Position first WHITE piece that gets jumped
	public static final Position WHITE_JUMPED_POSITION = new Position(
			RED_PAWN_ROW - 1,
			RED_PAWN_CELL + 1);

	// The end Position of a first single move
	public static final Position RED_PAWN_SINGLE_POSITION = new Position(
			RED_PAWN_ROW - 1,
			RED_PAWN_CELL + 1);

	// This is the Position of the WHITE piece that remains untouched
	public static final Position WHITE_SINGLE_PIECE_POSITION= new Position(0, 3);

	// These are placed here for convenience. Unit tests use them.
	public static final Move RED_FIRST_JUMP_MOVE = new Move(RED_PAWN_POSITION, RED_PAWN_JUMP_POSITION, null, Piece.Color.RED);

	public static final Move RED_SECOND_JUMP_MOVE = new Move(RED_PAWN_JUMP_POSITION, RED_PAWN_JUMP_SECOND_POSITION, null, Piece.Color.RED);

	public static final Move RED_FIRST_SINGLE_MOVE = new Move(RED_PAWN_POSITION, RED_PAWN_SINGLE_POSITION, null, Piece.Color.RED);


	/**
	 * A checkers board where the RED player can only move a single space
	 * but not affect any WHITE player pieces.
	 *
	 * . _ . W . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * R . _ . _ . _ .
	 *
	 * @return CheckersBoardBuilder
	 */
	public static CheckersBoardBuilder singleMove() {
		return CheckersBoardBuilder.aBoard()
				.withPieceAt(
						RED_SINGLE_PIECE,
						RED_PAWN_POSITION)
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						WHITE_SINGLE_PIECE_POSITION);
	}

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
					WHITE_SINGLE_PIECE,
					WHITE_JUMPED_POSITION)
			.withPieceAt(
					RED_SINGLE_PIECE,
					RED_PAWN_POSITION)
			.withPieceAt(
					WHITE_SINGLE_PIECE,
					new Position(4, 3))
			.withPieceAt(
					WHITE_SINGLE_PIECE,
					WHITE_SINGLE_PIECE_POSITION);
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
                        RED_SINGLE_PIECE,
                        new Position(7, 4));
	}

	/**
	 * Built off multiJump board, we place another RED piece on the board
	 * In the normal rules of checkers you must take the force jump and you should
	 * not be able to move the added RED piece
	 *
	 * . _ . W . _ . _
	 * _ . _ . _ . _ .
	 * . W . W . _ . _
	 * _ . _ . _ . _ .
	 * . _ . W . W . _
	 * _ . _ . _ . _ .
	 * . W . W . W . _
	 * R . _ . R . _ .
	 *
	 * @return CheckersBoardBuilder
	 */
	public static CheckersBoardBuilder forceAJumpWithOptions() {
		return forceAJump()
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						new Position(6, 3))
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						new Position(6, 5))
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						new Position(4, 5))
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						new Position(2, 3))
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						new Position(2, 1)
				)
				.withPieceAt(
						RED_KING_PIECE,
						new Position(7, 4));
	}

	/**
	 * Builds a board that is one turn away from making all the pieces on the
	 * ths field a king
	 *
	 * . _ . _ . _ . _
	 * _ . _ . _ . R .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . W . _
	 * R . _ . _ . _ .
	 *
	 * @return CheckersBoardBuilder
	 */
	public static CheckersBoardBuilder kingMe(){
		return CheckersBoardBuilder.aBoard()
				.withPieceAt(
						RED_SINGLE_PIECE,
						new Position(7, 0))
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						new Position(6, 5))
				.withPieceAt(
						RED_SINGLE_PIECE,
						new Position(1, 6))
				;
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
				.withoutPieceAt(WHITE_SINGLE_PIECE_POSITION);
	}

	/**
	 * For this board I did something special. I realized that we had too many magic numbers
	 * so I created some constants, and a constant red piece that tests can use to start their move
	 *
	 * BOARD
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . _ . _ . _ . _
	 * _ . _ . _ . _ .
	 * . W . _ . _ . _
	 * R . _ . _ . _ .
	 *
	 * @return CheckersBoardBuilder
	 */
	public static CheckersBoardBuilder singleJumpToEnd() {
		return CheckersBoardBuilder.aBoard()
				.withPieceAt(
						WHITE_SINGLE_PIECE,
						new Position(RED_PAWN_ROW - 1, RED_PAWN_CELL + 1)
				)
				.withPieceAt(
						RED_SINGLE_PIECE,
						RED_PAWN_POSITION
				);
	}
}
