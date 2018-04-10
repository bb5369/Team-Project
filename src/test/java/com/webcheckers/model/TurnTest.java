package com.webcheckers.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("Model-tier")
public class TurnTest {

	private static final int START_ROW = 2;
	private static final int START_CELL = 1;

	private static final int END_ROW = 3;
	private static final int END_CELL = 2;

	private static CheckersGame checkersGame;
	private static Space[][] checkersBoard;
	private static Player player;
	private Turn CuT;
	private static Move validMove;
	private static Move invalidMove;
	private static Move jumpMove;

	@BeforeAll
	public static void setupTest() {
		// build our real components
		player = new Player("Testy McTestFace");

		validMove = new Move(
				new Position(START_ROW, START_CELL),
				new Position(END_ROW, END_CELL)
		);

		invalidMove = new Move(
				new Position(START_ROW, START_CELL),
				new Position(3, 3)
		);

		jumpMove = new Move(
				new Position(START_ROW, START_CELL),
				new Position(START_ROW + 2, START_CELL + 2)
		);

		// create mocks
		checkersGame = mock(CheckersGame.class);
		when(checkersGame.getPlayerColor(player)).thenReturn(Piece.Color.WHITE);

	}

	/**
	 * We start each test with a new Turn for predicable state
	 */
	@BeforeEach
	public void setupCuT() {
		checkersBoard = CheckersBoardBuilder.aStartingBoard().getBoard();

		// TODO: Refactor this need out of MoveValidator so its passed in a board
		// MoveValidator will become a static class
		when(checkersGame.getBoard()).thenReturn(checkersBoard);

		CuT = new Turn(checkersGame, checkersBoard, player);

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}

	@Test
	public void constructor() {
		CuT = new Turn(checkersGame, checkersBoard, player);
	}

	@Test
	public void validateMove_valid() {
		assertTrue(CuT.validateMove(validMove));

		assertEquals(Turn.State.STABLE_TURN, CuT.getState());
	}

	@Test
	public void validateMove_invalid() {
		assertFalse(CuT.validateMove(invalidMove));

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}

	@Test
	@Disabled // We do not have jump moves working
	public void validateMove_jump() {
		Piece opponentPiece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
		Position jumpPosition = new Position(START_ROW+1, START_CELL+1);

		// Build a board with a piece to jump
		CheckersBoardBuilder jumpBoardBuilder = CheckersBoardBuilder.aStartingBoard()
				.withPieceAt(opponentPiece, jumpPosition);

		Space[][] board = jumpBoardBuilder.getBoard();
		when(checkersGame.getBoard()).thenReturn(board);

		// Setup a new turn with this board
		CuT = new Turn(checkersGame, board, player);

		assertTrue(CuT.validateMove(jumpMove));

		assertEquals(Turn.State.STABLE_TURN, CuT.getState());
	}


	@Test
	public void backupMove_single() {
		assertTrue(CuT.validateMove(validMove));

		assertTrue(CuT.backupMove());
		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}

	@Test
	public void backupMove_none() {
		assertFalse(CuT.backupMove());
		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}

	@Test
	@Disabled
	public void backupMove_multiple() {
		// TODO: move validator's concept of the board I think should update an interim
		// board while moves are being validated, since adding the same move shouldn't be valid

		Move jumpMoveSecond = new Move(
				new Position(START_ROW + 2, START_CELL + 2),
				new Position(START_ROW + 4, START_CELL + 4)
		);

		assertTrue(CuT.validateMove(jumpMove));
		assertTrue(CuT.validateMove(jumpMoveSecond));

		assertTrue(CuT.backupMove());
		assertEquals(Turn.State.STABLE_TURN, CuT.getState());


		assertTrue(CuT.backupMove());
		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}


	@Test
	public void itsMyTurn() {
		assertTrue(CuT.isMyTurn(player));
	}

	@Test
	public void valuedPlayer() {
		assertSame(player, CuT.getPlayer());
	}


}
