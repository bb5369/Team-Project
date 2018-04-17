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
	private static Piece.Color playerColor;
	private Turn CuT;
	private static Move validMove;
	private static Move invalidMove;
	private static Move jumpMove;

	@BeforeAll
	public static void setupTest() {
		// build our real components
		player = new Player("Testy McTestFace");
		playerColor = Piece.Color.RED;

		validMove = new Move(
				new Position(START_ROW, START_CELL),
				new Position(END_ROW, END_CELL),
				player, playerColor
		);

		invalidMove = new Move(
				TestCheckersBoards.RED_PAWN_POSITION,
				new Position(0, 0),
				player, playerColor
		);

		jumpMove = new Move(
				new Position(START_ROW, START_CELL),
				new Position(START_ROW + 2, START_CELL + 2),
				player, playerColor
		);

		// create mocks
		checkersGame = mock(CheckersGame.class);
	}

	/**
	 * We start each test with a new Turn for predicable state
	 * The Turn tests use the `singleJumpToEnd` board
	 */
	@BeforeEach
	public void setupCuT() {
		checkersBoard = TestCheckersBoards.singleJumpToEnd().getBoard();

		System.out.println("The board we are using for this test:");
		System.out.println(CheckersBoardHelper.formatBoardString(checkersBoard));

		CuT = new Turn(checkersBoard, player, playerColor);

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}

	@Test
	public void constructor() {
		CuT = new Turn(checkersBoard, player, playerColor);
	}

	/**
	 * This unit test ensures the Turn can advance to a stable situation in the JUMP_MOVE state
	 * We then roll back the move
	 */
	@Test
	public void validateMove_valid() {

		Message validateMoveMessage = CuT.validateMove(TestCheckersBoards.RED_FIRST_JUMP_MOVE);
		assertEquals(Message.MessageType.info, validateMoveMessage.getType());

		assertEquals(Turn.State.JUMP_MOVE, CuT.getState());

		assertTrue(CuT.backupMove());

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());

	}

	/**
	 * Given an invalid Move, Turn should fail validation and hold its EMPTY_TURN state
	 */
	@Test
	public void validateMove_invalid() {

		Message validateMoveMessage = CuT.validateMove(invalidMove);

		assertEquals(Message.MessageType.error, validateMoveMessage.getType());
		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}


	/**
	 * Given a valid jump move, I expect the latest board to have the jumped piece removed
	 */
	@Test
	public void validateMove_jump() {

		Space jumpedSpace = CheckersBoardHelper.getSpace(checkersBoard, TestCheckersBoards.WHITE_JUMPED_POSITION);

		Message validateMoveMessage = CuT.validateMove(TestCheckersBoards.RED_FIRST_JUMP_MOVE);
		assertEquals(Message.MessageType.info, validateMoveMessage.getType());
		assertEquals(Turn.State.JUMP_MOVE, CuT.getState());

		// Space is not affected on original board
		assertFalse(jumpedSpace.isOpen());

		Space[][] boardAfterJump = CuT.getLatestBoard();

		jumpedSpace = CheckersBoardHelper.getSpace(boardAfterJump, TestCheckersBoards.WHITE_JUMPED_POSITION);

		assertTrue(jumpedSpace.isOpen());
	}


	@Test
    @Disabled
	public void backupMove_single() {
		//assertTrue(CuT.validateMove(validMove));

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

		//assertTrue(CuT.validateMove(jumpMove));
		//assertTrue(CuT.validateMove(jumpMoveSecond));

		assertTrue(CuT.backupMove());
		//assertEquals(Turn.State.STABLE_TURN, CuT.getState());


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
