package com.webcheckers.model;

import java.util.PriorityQueue;

public class CheckersGame {

	//instance variables
	private final Player playerRed;
	private final Player playerWhite;
	private final Player playerActive;
	private final BoardView board;
	private PriorityQueue<Move> pendingValidMoves;
	private Space[][] matrix;

	/**
	 * Parameterized constructor
	 * Creation of a new checkers game between two places
	 *
	 * @param playerRed - Player one with red color pieces
	 * @param playerWhite - Player 2 wit white color pieces
	 */
	public CheckersGame(Player playerRed, Player playerWhite) {

		this.playerRed = playerRed;
		this.playerWhite = playerWhite;

		this.playerActive = this.playerRed;

		matrix = new Space[8][8];
		board = new BoardView();

		pendingValidMoves = new PriorityQueue<>();

		initializeMatrix();
	}

	/**
	 * Initializes the matrix of spaces to contain an initial board state of checkers
	 */
	public void initializeMatrix(){
		//These constants are used in here in RowGen building and in Move validation for sanity
		final Integer WHITE_BORDER_INDEX = 2;
		final Integer RED_BORDER_INDEX = 5;

		// Build row from Left to Right
		for(int row = 0; row < matrix.length; row++) {

			boolean startWhiteSquare = false;

			// Alternating rows begin with a dark space
			if(row % 2 == 0) {
				startWhiteSquare = true;
			}

			for (int i = 0; i < matrix[0].length; i++) {

				if (startWhiteSquare) {
					// we use startWhiteSquare to indicate a square that player cannot land on
					matrix[row][i] = new Space(i, Space.State.INVALID);

				} else {
					// A player can land on this Space, or Space filled with starting Piece

					if (row <= WHITE_BORDER_INDEX) {
						matrix[row][i] = new Space(i, new Piece(Piece.Type.SINGLE, Piece.Color.WHITE));

					} else if (row >= RED_BORDER_INDEX) {
						matrix[row][i] = new Space(i, new Piece(Piece.Type.SINGLE, Piece.Color.RED));

					} else {
						matrix[row][i] = new Space(i, Space.State.OPEN);
					}
				}
				// The next Space is inverted, ie a White wont follow a Black
				startWhiteSquare = !startWhiteSquare;
			}
		}
	}

	public Space[][] getMatrix(){
		return matrix;
	}

	public Player getPlayerRed() {
		return playerRed;
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}

	public Player getPlayerActive() {
		return playerActive;
	}

	/**
	 * What color is the given player?
	 * @param player
	 * @return Piece.Color
	 */
	public Piece.Color getPlayerColor(Player player) {
		if(player.equals(playerRed)) {
			return Piece.Color.RED;
		}
		else if (player.equals(playerWhite)) {
			return Piece.Color.WHITE;
		}
		else {
			return null;
		}
	}


	public BoardView getBoard() {
		return board;
	}

	private void clearValidMoves() {
		pendingValidMoves.clear();
	}

}
