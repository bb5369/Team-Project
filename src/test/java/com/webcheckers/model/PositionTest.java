package com.webcheckers.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class PositionTest {

	private static final int ROW = 1;
	private static final int CELL = 2;

	@BeforeAll
	public static void setupTest() {

	}

	@Test
	public void isThisAValueObjectOrJustInsanity() {
		Position CuT = new Position(ROW, CELL);

		assertEquals(ROW, CuT.getRow());
		assertEquals(CELL, CuT.getCell());
	}


	@Test
	public void absoluteDifferenceExpectations() {
		Position CuT = new Position(ROW, CELL);
		Position friend = new Position(ROW + 1, CELL + 1);

		Position difference = Position.absoluteDifference(CuT, friend);

		assertEquals(1, difference.getCell());
		assertEquals(1, difference.getRow());
	}

	@Test
	public void testToString() {
		Position CuT = new Position(ROW, CELL);

		String expected = new String("RowGen: 1 Column: 2");

		assertEquals(expected, CuT.toString());
	}

	@Test
	public void testMidPositon(){
		Move test = new Move(new Position(0,0), new Position(2, 2));
		Position mid = new Position(1,1);
		assertEquals(mid.toString(), test.getMidpoint().toString());
	}

	@Test void outOfBounds(){
		int low = -13;
		int high = 8;
		Position test = new Position(low, CELL);
		Position test2 = new Position(high, CELL);
		Position test3 = new Position(ROW, high);
		Position test4 = new Position(ROW, low);

		assertFalse(test.isOnBoard());
		assertFalse(test2.isOnBoard());
		assertFalse(test3.isOnBoard());
		assertFalse(test4.isOnBoard());
		assertTrue(new Position(ROW, CELL).isOnBoard());
	}
}
