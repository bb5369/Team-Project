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

	@BeforeAll
	public static void testSetup() {

		// Build dependent objects
		player = new Player(RED_PLAYER_NAME);
		piece = new Piece(Piece.Type.SINGLE, Piece.Color.WHITE);


		// Mock dependents
		game = mock(CheckersGame.class);

		// Setup behaviors
		when(game.getMatrix()).thenReturn(TestBoardBuilder.aBoard().build());
		when(game.getPlayerColor(player)).thenReturn(Piece.Color.WHITE);


		moveValidator = new MoveValidator(game, player);
	}

	@Test
	public void test_aDiagonalMove() {
		Move diagonalMove = new Move(
				new Position(2,1),
				new Position(3,0)
		);

		assertTrue(moveValidator.validateMove(diagonalMove));
	}

	@Test
	@Disabled
	public void test_aKingMoveBackwards() {
		// The board is laid out with white pieces starting on top
		// red pieces on bottom
		// so a white KING piece starting down on row 4 should be able to move up to row 3

		Piece king = new Piece(Piece.Type.KING, Piece.Color.WHITE);
		Position kingPosition = new Position(4, 1);

		Space[][] boardWithKing = TestBoardBuilder.aBoard().withPieceAt(king, kingPosition).build();

		when(game.getMatrix()).thenReturn(boardWithKing);

		Move kingMoveBackwards = new Move(
				kingPosition,
				new Position(3, 2)
		);

		assertTrue(moveValidator.validateMove(kingMoveBackwards));
	}
}
