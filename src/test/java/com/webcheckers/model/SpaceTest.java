package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("Model-tier")
public class SpaceTest {

	private static final int SPACE_ID = 0;

	/**
	 * Test that the single-arg constructor works without failure.
	 */
	@Test
	public void constructor_oneArg() {
		new Space(SPACE_ID);
	}

	/**
	 * Test that two arg constructor works without failure.
	 */
	@Test
	public void constructor_twoArg() {
		Piece mockPiece = mock(Piece.class);

		new Space(SPACE_ID, mockPiece);
	}

	/**
	 * Test that the space is not valid for moving
	 */
	@Test
	public void invalidSpace() {
		Space testSpace = new Space(SPACE_ID);

		assertFalse(testSpace.isValid());
	}

	/**
	 * Test that the space is actually valid
	 */
	@Test
	public void validSpace() {
		Piece mockPiece = mock(Piece.class);
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertTrue(testSpace.isValid());
	}

	/**
	 * Test that the Space maintains its cell ID
	 */
	@Test
	public void cellId() {
		Space testSpace = new Space(SPACE_ID);

		assertEquals(SPACE_ID, testSpace.getCellIdx());
	}

	/**
	 * Test that the Space returns a piece
	 */
	@Test
	public void spaceOccupied() {
		Piece mockPiece = mock(Piece.class);
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertSame(mockPiece, testSpace.getPiece());
	}
}
