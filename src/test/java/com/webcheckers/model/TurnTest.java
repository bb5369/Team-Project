package com.webcheckers.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class TurnTest {

	private static final int START_ROW = 2;
	private static final int START_CELL = 1;

	private static Space[][] checkersBoard;
	private static Player player;
	private static Piece.Color playerColor;
	private Turn CuT;
	private static Move invalidMove;

	@BeforeAll
	public static void setupTest() {
		player = new Player("Testy McTestFace");
		playerColor = Piece.Color.RED;

		invalidMove = new Move(
				TestCheckersBoards.RED_PAWN_POSITION,
				new Position(0, 0),
				player, playerColor
		);
	}

	/**
	 * We start each test with a new Turn for predicable state
	 * The Turn tests use the `singleJumpToEnd` board
	 * This also has the benefit of testing the constructor
	 */
	@BeforeEach
	public void setupCuT() {
		checkersBoard = TestCheckersBoards.singleJumpToEnd().getBoard();

		CuT = new Turn(checkersBoard, player, playerColor);

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
	}

	@Test
	public void test_player_setup() {
		assertTrue(CuT.isMyTurn(player));

		Message isFinalizedMessage = CuT.isFinalized();
		assertEquals(Message.MessageType.error, isFinalizedMessage.getType());
		assertEquals(Turn.NO_MOVES_MSG, isFinalizedMessage.getText());

		assertEquals(player, CuT.getPlayer());
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
		assertEquals(Turn.VALID_JUMP_MOVE_MSG, validateMoveMessage.getText());

		assertEquals(Turn.State.JUMP_MOVE, CuT.getState());

		// Space is not affected on original board
		assertFalse(jumpedSpace.isOpen());

		Space[][] boardAfterJump = CuT.getLatestBoard();

		jumpedSpace = CheckersBoardHelper.getSpace(boardAfterJump, TestCheckersBoards.WHITE_JUMPED_POSITION);

		assertTrue(jumpedSpace.isOpen());
	}

	/**
	 * Given a valid single-space move, I expect the latest board to have the moved piece
	 */
	@Test
	public void validateMove_single() {
		checkersBoard = TestCheckersBoards.singleMove().getBoard();

		CuT = new Turn(checkersBoard, player, playerColor);

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());

		Message validateMoveMessage = CuT.validateMove(TestCheckersBoards.RED_FIRST_SINGLE_MOVE);
		assertEquals(Message.MessageType.info, validateMoveMessage.getType());
		assertEquals(Turn.State.SINGLE_MOVE, CuT.getState());

		Space[][] boardAfterMove = CuT.getLatestBoard();

		Space sourceSpace = CheckersBoardHelper.getSpace(boardAfterMove, TestCheckersBoards.RED_PAWN_POSITION);
		Space destinationSpace = CheckersBoardHelper.getSpace(boardAfterMove, TestCheckersBoards.RED_PAWN_SINGLE_POSITION);

		assertTrue(sourceSpace.isOpen());
		assertEquals(TestCheckersBoards.RED_SINGLE_PIECE, destinationSpace.getPiece());


		// Test part 2 - A single-move turn should now allow another move
		// The parameters of the move shouldn't matter since it is caught before its sent to the move validator
		validateMoveMessage = CuT.validateMove(invalidMove);

		assertEquals(Message.MessageType.error, validateMoveMessage.getType());
		assertEquals(Turn.SINGLE_MOVE_ONLY_MSG, validateMoveMessage.getText());

		Message isFinalizedMessage = CuT.isFinalized();
		assertEquals(Message.MessageType.info, isFinalizedMessage.getType());
		assertEquals(Turn.TURN_FINALIZED_MSG, isFinalizedMessage.getText());
	}


	/**
	 */
	@Test
	public void validateMove_forceJump() {

		System.out.println("Using board [forceAJump]");
		checkersBoard = TestCheckersBoards.forceAJump().getBoard();
		CuT = new Turn(checkersBoard, player, playerColor);

		// Part 1 - If a player has a jump move available, they must take it.

		// try a single move
		Move move = new Move(new Position(7, 4), new Position(6, 3));

		Message validateMoveMessage = CuT.validateMove(move);
		assertEquals(Message.MessageType.error, validateMoveMessage.getType());
		assertEquals(Turn.JUMP_MOVE_AVAIL_MSG, validateMoveMessage.getText());

		// Part 2 - A player must finish a series of jumps
		validateMoveMessage = CuT.validateMove(TestCheckersBoards.RED_FIRST_JUMP_MOVE);
		assertEquals(Message.MessageType.info, validateMoveMessage.getType());
		assertEquals(Turn.VALID_JUMP_MOVE_MSG, validateMoveMessage.getText());

		// now try to make a single move that isn't completing the multi-jump
		Move moveInWrongDirection = new Move(
				TestCheckersBoards.RED_PAWN_JUMP_POSITION,
				new Position(TestCheckersBoards.RED_PAWN_ROW - 3, TestCheckersBoards.RED_PAWN_CELL + 1));

		System.out.println(CheckersBoardHelper.formatBoardString(CuT.getLatestBoard()));

		validateMoveMessage = CuT.validateMove(moveInWrongDirection);
		assertEquals(Message.MessageType.error, validateMoveMessage.getType());
		assertEquals(Turn.JUMP_MOVE_ONLY_MSG, validateMoveMessage.getText());

		// test that the move is not finalized (there is ar remaining jump move)
		Message isFinalizedMessage = CuT.isFinalized();
		assertEquals(Message.MessageType.error, isFinalizedMessage.getType());
		assertEquals(Turn.JUMP_MOVE_PARTIAL_MSG, isFinalizedMessage.getText());

		// now finish the jump move
		validateMoveMessage = CuT.validateMove(TestCheckersBoards.RED_SECOND_JUMP_MOVE);
		assertEquals(Message.MessageType.info, validateMoveMessage.getType());
		assertEquals(Turn.VALID_JUMP_MOVE_MSG, validateMoveMessage.getText());

		isFinalizedMessage = CuT.isFinalized();
		assertEquals(Message.MessageType.info, isFinalizedMessage.getType());
		assertEquals(Turn.TURN_FINALIZED_MSG, isFinalizedMessage.getText());


	}


	@Test
	public void backupMove_jump() {
		Message validateMoveMessage = CuT.validateMove(TestCheckersBoards.RED_FIRST_JUMP_MOVE);

		assertEquals(Message.MessageType.info, validateMoveMessage.getType());
		assertEquals(Turn.VALID_JUMP_MOVE_MSG, validateMoveMessage.getText());
		assertFalse(CuT.canResign());

		assertTrue(CuT.backupMove());

		assertEquals(Turn.State.EMPTY_TURN, CuT.getState());
		assertTrue(CuT.canResign());
	}

	@Test
	public void backupMove_none() {
		assertFalse(CuT.backupMove());
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
