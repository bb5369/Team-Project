package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.webcheckers.model.CheckersBoardHelper.*;

@Tag("Model-tier")
public class CheckersBoardHelperTest {

	@Test
	public void getSpace_success() {

		Piece piece = new Piece(Piece.Type.SINGLE, Piece.Color.RED);
		Position position = new Position(0, 1);

		Space[][] board = CheckersBoardBuilder.aBoard()
				.withPieceAt(piece, position)
				.getBoard();

		Space space = getSpace(board, position);

		assertEquals(piece, space.getPiece());
		assertSame(piece, space.getPiece());
	}


	@Test
	public void formatBoardString_startingBoard() {
		String expectedOutput = String.join(
				"\n",
            "BOARD",
            ". W . W . W . W",
            "W . W . W . W .",
            ". I . W . W . W",
            "_ . _ . _ . _ .",
            ". _ . _ . _ . _",
            "K . R . R . R .",
            ". R . R . R . R",
            "R . R . R . R .");

		Piece redKing = new Piece(Piece.Type.KING, Piece.Color.RED);
		Piece whiteKing = new Piece(Piece.Type.KING, Piece.Color.WHITE);

		Space[][] board = CheckersBoardBuilder.aStartingBoard()
				.withPieceAt(redKing, new Position(5, 0))
				.withPieceAt(whiteKing, new Position(2, 1))
				.getBoard();

		assertEquals(expectedOutput, formatBoardString(board));
	}

}
