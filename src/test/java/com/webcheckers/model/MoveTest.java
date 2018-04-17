package com.webcheckers.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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

	private Move CuT;

	@BeforeAll
	public void setupTest() {
		start = new Position(START_ROW, START_CELL);
		end   = new Position(END_ROW, END_CELL);

		CuT = new Move(start, end);
		CuT.setPlayer(new Player(name));
		CuT.setPieceColor(Piece.Color.RED);
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
	public void areWeAJumpMove() {
		assertTrue(CuT.isJump());
		assertTrue(CuT.isDiagonal());
		assertTrue(CuT.isValid());
		assertFalse(CuT.isSingleSpace());
	}

	@Test
	public void weAreNotAJumpMove() {
		Position endFriend = new Position(START_ROW + 1, START_CELL + 1);

		Move notAJumpMove = new Move(start, endFriend);

		assertFalse(notAJumpMove.isJump());
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
		assertTrue(new Move(endAway, endAway2).isValid());
		assertFalse(alsoNotAJumpMove.isDiagonal());
	}

	@Test
	public void testToString() {
		String expected = new String("<1,1> to <3,3>");

		assertEquals(expected, CuT.toString());
	}

	@Test
	public void testOtherConstruct(){
		Position bad = new Position(-1, 0);
		Move move = new Move(start, bad, new Player(name), Piece.Color.WHITE);

		assertFalse(move.isValid());
		move = new Move(bad, end, new Player(name), Piece.Color.WHITE);
		assertFalse(move.isValid());
		move = new Move(start, new Position(START_ROW + 3, START_CELL + 3), new Player(name), Piece.Color.WHITE);
		assertFalse(move.isValid());

	}
}
