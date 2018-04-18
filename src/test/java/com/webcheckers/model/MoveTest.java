package com.webcheckers.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoveTest {

	private static final int START_ROW = 1;
	private static final int START_CELL = 1;
	private static final int END_ROW = 3;
	private static final int END_CELL = 3;

	private Position start;
	private Position end;
	private String name = "test";

	private Move CuT, single;

	@BeforeAll
	public void setupTest() {
		start = new Position(START_ROW, START_CELL);
		end   = new Position(END_ROW, END_CELL);

		CuT = new Move(start, end);
		CuT.setPlayer(new Player(name, Player.GameType.NORMAL));
		CuT.setPieceColor(Piece.Color.RED);
		single = new Move(start, new Position(2,2));
		CuT.setPieceColor(Piece.Color.RED);
		CuT.setPlayer(new Player("test", Player.GameType.NORMAL));
	}

	@Test
	public void whatAreMyValuesReallyWorth() {

		assertEquals(start, CuT.getStart());
		assertEquals(end, CuT.getEnd());

		assertEquals(START_ROW, CuT.getStartRow());
		assertEquals(START_CELL, CuT.getStartCell());

		assertEquals(END_ROW, CuT.getEndRow());
		assertEquals(END_CELL, CuT.getEndCell());

		assertEquals(Piece.Color.RED, CuT.getPieceColor());
		assertEquals(name, CuT.getPlayerName());
	}

	@Test
	@Disabled
	public void areWeAJumpMove() {
		assertTrue(CuT.isJump());
		//assertTrue(CuT.isDiagonal());
		assertTrue(CuT.isValid());
		assertFalse(CuT.isSingleSpace());
		assertTrue(CuT.isValid());
		assertFalse(single.isValid());
		assertEquals(CuT.getMidpoint().toString(), new Position(2,2).toString());
	}

	@Test
	@Disabled
	public void weAreNotAJumpMove() {
		Position endFriend = new Position(START_ROW + 1, START_CELL + 1);

		Move notAJumpMove = new Move(start, endFriend);

		assertFalse(notAJumpMove.isJump());
		assertFalse(notAJumpMove.isValid());
	}

	@Test
	public void weAreLongWayFromHome() {
		Position endAway = new Position(START_ROW + 1, START_CELL + 2);
		Position endAway2 = new Position(START_ROW + 2, START_CELL + 1);

		Move alsoNotAJumpMove = new Move(start, endAway);

		assertFalse(alsoNotAJumpMove.isJump());
		assertFalse(alsoNotAJumpMove.isValid());
		assertFalse(alsoNotAJumpMove.isSingleSpace());
		assertFalse(new Move(start, endAway2).isSingleSpace());
		assertTrue(new Move(endAway, endAway2).isSingleSpace());
		assertFalse(new Move(endAway, endAway2).isValid());
		//assertFalse(alsoNotAJumpMove.isDiagonal());
	}

	@Test
	public void test(){
		assertEquals(single.getStart(), single.getMidpoint());
		assertFalse(CuT.isSingleSpace());
		assertTrue(single.isSingleSpace());
		Move test = new Move(start, new Position(START_ROW, START_CELL+ 1));
		assertFalse(test.isSingleSpace());
		//assertFalse(test.isDiagonal());
		test = new Move(start, new Position(START_ROW + 1, START_CELL));
		assertFalse(test.isSingleSpace());
		assertFalse(test.isValid());
	}

	@Test
	public void get_set(){
		//String name = "bodkj";
		Player test = new Player(name, Player.GameType.NORMAL);
		CuT.setPlayer(test);
		assertEquals(test.getName(), name);
		Piece.Color color = Piece.Color.RED;
		CuT.setPieceColor(color);
		Move move = new Move(CuT.getStart(), CuT.getEnd(), test, color);
		assertEquals(CuT.getPieceColor(), color);
		CuT.setPieceColor(color);
		CuT.setPlayer(test);
		assertEquals(move.toString(), CuT.toString());
		assertEquals(CuT.getPlayerName(), name);
	}

	@Test
	public void inValid(){
		Move test = new Move(start, new Position(-1, 0));
		assertFalse(test.isValid());
		test = new Move(new Position(-1,0), end);
		assertFalse(test.isValid());
		test = new Move(new Position(0,0), new Position(0,2));
		assertFalse(test.isValid());
	}

	@Test
	public void testToString() {
		String expected = new String("<1,1> to <3,3>");

		assertEquals(expected, CuT.toString());
	}

	@Test
	public void testOtherConstruct() {
		Position bad = new Position(-1, 0);
		Move move = new Move(start, bad, new Player(name, Player.GameType.NORMAL), Piece.Color.WHITE);

		assertFalse(move.isValid());
		move = new Move(bad, end, new Player(name, Player.GameType.NORMAL), Piece.Color.WHITE);
		assertFalse(move.isValid());
		move = new Move(start, new Position(START_ROW + 3, START_CELL + 3), new Player(name, Player.GameType.NORMAL), Piece.Color.WHITE);
		move = new Move(bad, end, new Player(name, Player.GameType.NORMAL), Piece.Color.WHITE);
		assertFalse(move.isValid());
		move = new Move(start, new Position(START_ROW + 3, START_CELL + 3), new Player(name, Player.GameType.NORMAL), Piece.Color.WHITE);
		assertTrue(move.isValid());
	}

	@Test
	public void testFail(){
		Move move = new Move(start,end);
		move.setPieceColor(Piece.Color.RED);
		assertFalse(move.isValid());
	}
}
