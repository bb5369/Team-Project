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

	private Move CuT;

	@BeforeAll
	public void setupTest() {
		start = new Position(START_ROW, START_CELL);
		end   = new Position(END_ROW, END_CELL);

		CuT = new Move(start, end);
	}

	@Test
	public void whatAreMyValuesReallyWorth() {

		assertEquals(start, CuT.getStart());
		assertEquals(end, CuT.getEnd());

		assertEquals(START_ROW, CuT.getStartRow());
		assertEquals(START_CELL, CuT.getStartCell());

		assertEquals(END_ROW, CuT.getEndRow());
		assertEquals(END_CELL, CuT.getEndCell());
	}

	@Test
	public void areWeAJumpMove() {
		assertTrue(CuT.isAJumpMoveAttempt());
	}

	@Test
	public void weAreNotAJumpMove() {
		Position endFriend = new Position(START_ROW + 1, START_CELL + 1);

		Move notAJumpMove = new Move(start, endFriend);

		assertFalse(notAJumpMove.isAJumpMoveAttempt());
	}

	@Test
	public void weAreLongWayFromHome() {
		Position endAway = new Position(START_ROW + 1, START_CELL + 2);

		Move alsoNotAJumpMove = new Move(start, endAway);

		assertFalse(alsoNotAJumpMove.isAJumpMoveAttempt());
	}

	@Test
	public void testToString() {
		String expected = new String("<1,1> to <3,3>");

		assertEquals(expected, CuT.toString());
	}
}
