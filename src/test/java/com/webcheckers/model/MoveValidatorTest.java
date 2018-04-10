package com.webcheckers.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Model-tier")
public class MoveValidatorTest {

	private static final String RED_PLAYER_NAME = "redPlayer";
	private static final String WHITE_PLAYER_NAME = "whitePlayer";

	private static CheckersGame game;
	private static Player player;
	private static Piece piece;

	private static MoveValidator moveValidator;

	private static CheckersBoardBuilder boardBuilder;

	private Space[][] board;

	@BeforeAll
	public static void testSetup() {

		// Build dependent objects
		player = new Player(RED_PLAYER_NAME);
		piece = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);


		// Mock dependents
		game = mock(CheckersGame.class);

		boardBuilder = CheckersBoardBuilder.aBoard();


		// Setup behaviors
		when(game.getBoard()).thenReturn(boardBuilder.build());
		when(game.getPlayerColor(player)).thenReturn(Piece.Color.WHITE);


		moveValidator = new MoveValidator(player, Piece.Color.WHITE);
	}

	@Test
	public void test_aDiagonalMove() {
		board = boardBuilder.build();

		Move diagonalMove = new Move(
				new Position(2,1),
				new Position(3,0)
		);

		assertTrue(moveValidator.validateMove(board, diagonalMove));
	}

	@Test
	@Disabled
	public void test_aKingMoveBackwards() {
		board = boardBuilder.build();

		// The board is laid out with white pieces starting on top
		// red pieces on bottom
		// so a white KING piece starting down on row 4 should be able to move up to row 3

		Piece king = new Piece(Piece.Type.KING, Piece.Color.WHITE);
		Position kingPosition = new Position(4, 1);

		Space[][] boardWithKing = CheckersBoardBuilder.aBoard().withPieceAt(king, kingPosition).build();

		when(game.getBoard()).thenReturn(boardWithKing);

		Move kingMoveBackwards = new Move(
				kingPosition,
				new Position(3, 2)
		);

		assertTrue(moveValidator.validateMove(board, kingMoveBackwards));
	}
}
