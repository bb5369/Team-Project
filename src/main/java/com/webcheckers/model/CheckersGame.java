package com.webcheckers.model;

public class CheckersGame {

	//instance variables
	private final Player playerRed;
	private final Player playerWhite;

	/**
	 * Parameterized constructor
	 * this is used to instantiate CheckerGame objects
	 *
	 * @param playerRed - Player one with red color pieces
	 * @param playerWhite - Player 2 wit white color pieces
	 */
	public CheckersGame(Player playerRed, Player playerWhite) {
		this.playerRed = playerRed;
		this.playerWhite = playerWhite;
	}

	public Player getPlayerRed() {
		return playerRed;
	}

	public Player getPlayerWhite() {
		return playerWhite;
	}
}
