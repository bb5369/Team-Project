package com.webcheckers.model;

import com.webcheckers.util.DoublyLinkedQueue;

public class CheckersGame {

	//instance variables
	private final Player playerRed;
	private final Player playerWhite;
	private final Player resignedPlayer;
	private Player playerActive;
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

		this.resignedPlayer = null;
		generateStartingBoard();
	}

	public CheckersGame(CheckersGame game, Player player){
		this.playerRed = game.getPlayerRed();
		this.playerWhite = game.getPlayerWhite();

		this.resignedPlayer = player;
	}


	/**
	 * getMatrix method--
	 * space matrix representing a checkers board
	 * @return space matrix
	 */
	public Space[][] getMatrix(){
		return matrix;
	}

	/**
	 * getPlayerRed method--
	 * used to access the red player in the game
	 * @return - Red player in the game
	 */
	public Player getPlayerRed() {
		return playerRed;
	}

	/**
	 * getWhitePlayer method--
	 * Used to access the white player in the game
	 * @return - White player in the game
	 */
	public Player getPlayerWhite() {
		return playerWhite;
	}

	public Player getOtherPlayer(Player player){
		if(playerRed.equals(player)){
			return playerWhite;
		}
		else{
			return playerRed;
		}
	}

	/**
	 * getPlayerActive method--
	 * Used to access player whose turn it is
	 * @return player whose turn it is
	 */
	public Player getPlayerActive() {
		return playerActive;
	}

	/**
	 * changeActivePlayer method--
	 * This method changes the state of active player
	 */
	public void changeActivePlayer()
	{
		if(playerActive.equals(playerRed))
		{
			playerActive = playerWhite;
		}
		else
		{
			playerActive = playerRed;
		}
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

	public boolean isResignedPlayer (Player player){
		if(resignedPlayer == null){
			return false;
		}
		else{
			return this.resignedPlayer.equals(player);
		}
	}


	/**
	 * Uses our static BoardBuilder to generate the starting Checkers Board
	 */
	private void generateStartingBoard() {
		this.matrix = BoardBuilder.buildBoard();
	}
}
