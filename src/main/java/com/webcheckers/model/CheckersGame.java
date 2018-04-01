package com.webcheckers.model;

import com.webcheckers.util.DoublyLinkedQueue;

import java.util.PriorityQueue;

public class CheckersGame {

	//instance variables
	private final Player playerRed;
	private final Player playerWhite;
	private final Player resignedPlayer;
	private Player playerActive;
	private DoublyLinkedQueue<Move> validMoves;
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
		initializeMatrix();
	}

	public CheckersGame(Player playerRed, Player playerWhite, Player playerActive, Space[][] matrix){
		this.playerRed = playerRed;
		this.playerWhite = playerWhite;

		this.playerActive = playerActive;
		this.resignedPlayer = null;

		initializeMatrix();
	}

	public CheckersGame(CheckersGame game, Player player){
		this.playerRed = game.getPlayerRed();
		this.playerWhite = game.getPlayerWhite();

		this.playerActive = game.getPlayerActive();

		this.resignedPlayer = player;
		validMoves = new DoublyLinkedQueue<>();

		initializeMatrix();
	}

	/**
	 * getValidMoves method--
	 * Used to access the list of validMoves
	 * @return a list of valid moves (DoublyLinkedQueue)
	 */
	public DoublyLinkedQueue getValidMoves()
	{
		return validMoves;
	}

	/**
	 * Initializes the matrix of spaces to contain an initial board state of checkers
	 */
	public void initializeMatrix(){
		matrix = new Space[8][8];

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

	public CheckersGame remove(CheckersGame game, Player player){
		Player active;
		if(playerRed.equals(player)){
			if(game.getPlayerActive().equals(player))
				active = player;
			else
				active = game.getPlayerWhite();
			return new CheckersGame(new Player("null"), game.getPlayerWhite(), active, matrix);
		}
		else {
			if(game.getPlayerActive().equals(player))
				active = player;
			else
				active = game.getPlayerWhite();
			return new CheckersGame(game.getPlayerRed(), new Player("null"), active, matrix);
		}
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
	 * clearValidMoves method--
	 * This method clears all the valid moves list
	 */
	private void clearValidMoves() {
		validMoves.removeAll();
	}
}
