package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("Model-tier")
public class SpaceTest {

	private static final int SPACE_ID = 0;
	private static Piece mockPiece;


	@BeforeAll
	public static void setupMocks() {
		// internal model mocks
		mockPiece = mock(Piece.class);
	}

	/**
	 * Test that the constructor using a piece works as expected
	 */
	@Test
	public void constructor_piece() {
		new Space(SPACE_ID, mockPiece);
	}



	/**
	 * Test that the state constructor works as expected
	 */
	@Test
	public void constructor_state() {
		new Space(SPACE_ID, Space.State.INVALID);
	}

	/**
	 * Test that the piece constructors throws a null exception
	 */
	@Test
	public void constructor_null_piece() {
		Piece nullPiece = null;

		assertThrows(NullPointerException.class, () -> {
			new Space(SPACE_ID, nullPiece);
		});
	}

	/**
	 * Test that the space is not valid for a piece to move to
	 */
	@Test
	public void invalidSpace() {
		Space testSpace = new Space(SPACE_ID, Space.State.INVALID);

		assertFalse(testSpace.isValid());

	}

	/**
	 * Test that the space is open
	 */
	@Test
	public void validSpacePiece() {
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertTrue(testSpace.isValid());
	}

	/**
	 * Test if a space is open for a piece to move to
	 */
	@Test
	public void isOpen() {
		Space testSpace = new Space(SPACE_ID, Space.State.OPEN);

		assertTrue(testSpace.isOpen());
	}

	/**
	 * Test if a space is not open
	 */
	@Test
	public void isNotOpen() {
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertFalse(testSpace.isOpen());
	}

	/**
	 * Test if a space is occupied
	 */
	@Test
	public void isOccupied() {
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertTrue(testSpace.isOccupied());
	}

	/**
	 * Test if a space is not occupied
	 */
	@Test
	public void isNotOccupied() {
		Space testSpace = new Space(SPACE_ID, Space.State.OPEN);

		assertFalse(testSpace.isOccupied());
	}

	/**
	 * Test that a space retains its cell ID
	 */
	@Test
	public void cellIdAmnesia() {
		Space testSpace = new Space(SPACE_ID, Space.State.OPEN);

		assertEquals(SPACE_ID, testSpace.getCellIdx());
	}

	/**
	 * Test that the Space returns a piece
	 */
	@Test
	public void spacePiece() {
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertSame(mockPiece, testSpace.getPiece());
	}

	/**
	 * Test that a Space knows its state
	 */
	@Test
	public void spaceState() {
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertSame(Space.State.OCCUPIED, testSpace.getState());
	}

	/**
	 * Test that a Piece can be added to a Space
	 */
	@Test
	public void addPiece() {
		Space testSpace = new Space(SPACE_ID, Space.State.OPEN);

		testSpace.addPiece(mockPiece);

		assertSame(mockPiece, testSpace.getPiece());
	}

	@Test
	public void addPiece_invalid() {
		Space testSpace = new Space(SPACE_ID, Space.State.INVALID);

		Space.State result = testSpace.addPiece(mockPiece);

		assertEquals(Space.State.INVALID, result);
	}

	@Test
	public void removePiece() {
		Space testSpace = new Space(SPACE_ID, mockPiece);

		testSpace.removePiece();

		assertEquals(Space.State.OPEN, testSpace.getState());
		assertNull(testSpace.getPiece());
	}

	@Test
	public void removePiece_alreadyEmpty() {
		Space testSpace = new Space(SPACE_ID, Space.State.OPEN);

		assertEquals(Space.State.OPEN, testSpace.removePiece());

	}

	@Test
	public void movePieceFrom_nullSource() {
		Space sourceSpace = null;
		Space testSpace = new Space(SPACE_ID, mockPiece);

		assertFalse(testSpace.movePieceFrom(sourceSpace));
	}

	@Test
	public void movePieceFrom_destinationNotOpen() {
		Space sourceSpace = new Space(SPACE_ID, mockPiece);

		Space testSpace = new Space(SPACE_ID, Space.State.INVALID);

		assertFalse(testSpace.movePieceFrom(sourceSpace));
	}

	@Test
	public void movePieceFrom_sourceNoPiece() {
		Space sourceSpace = new Space(SPACE_ID, Space.State.OPEN);

		Space testSpace = new Space(SPACE_ID, Space.State.OPEN);

		assertFalse(testSpace.movePieceFrom(sourceSpace));
	}

	@Test
	public void movePieceFrom() {
		Space sourceSpace = new Space(SPACE_ID, mockPiece);

		Space testSpace = new Space(SPACE_ID, Space.State.OPEN);

		assertTrue(testSpace.movePieceFrom(sourceSpace));
	}
}
