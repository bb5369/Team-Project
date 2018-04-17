package com.webcheckers.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Model-tier")
public class MoveValidatorTest {

	private static final String RED_PLAYER_NAME = "redPlayer";
	private static final String WHITE_PLAYER_NAME = "whitePlayer";

	private static Player player;

	private static CheckersBoardBuilder boardBuilder;

	private Space[][] board;
	private Move jumpOne, jumpTwo;

	@BeforeEach
	public void testSetup() {

		// Build dependent objects
		player = new Player(WHITE_PLAYER_NAME);
		jumpOne = new Move(new Position(0,0), new Position(2,2));
		jumpTwo = new Move(jumpOne.getEnd(), new Position(4,4));
		jumpOne.setPieceColor(Piece.Color.RED);

		boardBuilder = CheckersBoardBuilder.aStartingBoard();
	}

	@Test
	public void test_aDiagonalMove() {
		board = boardBuilder.getBoard();

		Move diagonalMove = new Move(
				new Position(2,1),
				new Position(3,0),
				player,
				Piece.Color.WHITE
		);

		assertTrue(MoveValidator.validateMove(board, diagonalMove));
	}

	@Test
	public void test_aKingMoveBackwards() {
		board = boardBuilder.getBoard();

		// The board is laid out with white pieces starting on top
		// red pieces on bottom
		// so a white KING piece starting down on row 4 should be able to move up to row 3

		Piece king = new Piece(Piece.Type.KING, Piece.Color.WHITE);
		Position kingPosition = new Position(4, 1);

		Space[][] boardWithKing = CheckersBoardBuilder.aBoard().withPieceAt(king, kingPosition).getBoard();


		Move kingMoveBackwards = new Move(
				kingPosition,
				new Position(3, 2),
				player,
				Piece.Color.WHITE
		);
		//assertTrue(MoveValidator.areMovesAvailableForPlayer(board,player, kingMoveBackwards.getPieceColor()));
		assertTrue(MoveValidator.validateMove(boardWithKing, kingMoveBackwards));
	}

	@Test
	public void test_areMovesAvailable(){
		Piece.Color color = Piece.Color.WHITE;
		Space[][] board = TestCheckersBoards.singleJumpToEnd().getBoard();
		// Tests against a starting board
		assertTrue(MoveValidator.areMovesAvailableForPlayer(board, player, color));
		board = CheckersBoardBuilder.aBoard().getBoard();
		assertFalse(MoveValidator.areMovesAvailableForPlayer(board, player, color));
	}

	@Test
	public void test_jumpMove(){
		Space[][] board = TestCheckersBoards.multiJumpToEnd().getBoard();
		assertTrue(MoveValidator.areJumpsAvailableForPlayer(board, jumpOne.getPieceColor()));
		assertFalse(MoveValidator.areJumpsAvailableForPlayer(board, Piece.Color.WHITE));
		assertFalse(MoveValidator.canContinueJump(board, jumpOne.getEnd(), jumpOne.getPieceColor()));
		Move test = new Move(jumpOne.getStart(), new Position(1,0));
		test.setPieceColor(Piece.Color.RED);
		test.setPlayer(new Player(RED_PLAYER_NAME));
		//assertFalse(MoveValidator.validateMove(board, test));
	}

}
