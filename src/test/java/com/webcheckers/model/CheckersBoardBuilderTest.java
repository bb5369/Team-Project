package com.webcheckers.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class CheckersBoardBuilderTest {

	private static Space[][] CheckersBoard;

	@BeforeAll
	public static void setupTest() {

		CheckersBoard = CheckersBoardBuilder.aStartingBoard().build();
	}

	@Test
	public void invalidSpaces() {

		// Board starts at 0,0 with an invalid space
		boolean invalid = true;

		for (int row = 0; row < CheckersBoardBuilder.ROWS; row++) {

			for (int cell = 0; cell < CheckersBoardBuilder.CELLS; cell++) {

				Space space = CheckersBoard[row][cell];

				if (invalid) {
					assertEquals(Space.State.INVALID, space.getState());
				} else {
					assertNotEquals(Space.State.INVALID, space.getState());
				}

				invalid = !invalid;
			}

			invalid = !invalid;
		}
	}

	@Test
	public void whiteStartingPieces() {

		// Board starts at 0,0 with an invalid space
		boolean invalid = true;

		for (int row = 0; row <= CheckersBoardBuilder.WHITE_BORDER_INDEX; row++) {

			for (int cell = 0; cell < CheckersBoardBuilder.CELLS; cell++) {

				Space space = CheckersBoard[row][cell];

				if (!invalid) {
					assertEquals(Space.State.OCCUPIED, space.getState());

					assertEquals(Piece.Color.WHITE, space.getPiece().getColor());

				}

				invalid = !invalid;
			}

			invalid = !invalid;
		}
	}

	@Test
	public void redStartingPieces() {

		// 5th row starts with a valid Space with Red Piece
		boolean invalid = false;

		for (int row = CheckersBoardBuilder.RED_BORDER_INDEX; row < CheckersBoardBuilder.ROWS; row++) {

			for (int cell = 0; cell < CheckersBoardBuilder.CELLS; cell++) {

				Space space = CheckersBoard[row][cell];

				if (!invalid) {
					assertEquals(Space.State.OCCUPIED, space.getState());

					assertEquals(Piece.Color.RED, space.getPiece().getColor());

				}

				invalid = !invalid;
			}

			invalid = !invalid;
		}
	}
}
