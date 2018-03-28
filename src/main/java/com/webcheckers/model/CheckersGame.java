package com.webcheckers.model;

import java.util.PriorityQueue;

public class CheckersGame {

	//instance variables
	private final Player playerRed;
	private final Player playerWhite;
	private final Player playerActive;
	private final BoardView board;
	private PriorityQueue<Move> pendingValidMoves;

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

		board = new BoardView();

		pendingValidMoves = new PriorityQueue<>();
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
