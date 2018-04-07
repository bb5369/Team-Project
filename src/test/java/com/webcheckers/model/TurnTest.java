package com.webcheckers.model;

import com.webcheckers.ui.WebServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
		checkersBoard = BoardBuilder.buildBoard();

		// TODO: Refactor this need out of MoveValidator so its passed in a board
		// MoveValidator will become a static class
		when(checkersGame.getMatrix()).thenReturn(checkersBoard);

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
	public void submitTurn_emptyQueue() {
		assertFalse(CuT.submitTurn());

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}

	@Test
	public void submitTurn_success() {
		Piece startPiece = checkersBoard[START_ROW][START_CELL].getPiece();

		assertTrue(CuT.validateMove(validMove));
		assertEquals(Turn.State.STABLE_TURN, CuT.getState());
		assertFalse(CuT.isSubmitted());

		assertTrue(CuT.submitTurn());

		assertTrue(CuT.isSubmitted());
		verify(checkersGame, times(1)).nextTurn();

		Piece endPiece = checkersBoard[END_ROW][END_CELL].getPiece();
		assertSame(startPiece, endPiece);

	}

	@Test
	public void validateMove_jump() {
		assertTrue(CuT.validateMove(jumpMove));

		assertEquals(Turn.State.STABLE_TURN, CuT.getState());

		assertTrue(CuT.submitTurn());
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
	public void backupMove_multiple() {
		// TODO: move validator's concept of the board I think should update an interim
		// board while moves are being validated, since adding the same move shouldn't be valid
		assertTrue(CuT.validateMove(validMove));
		assertFalse(CuT.validateMove(validMove));

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
