package com.webcheckers.model;

import java.util.PriorityQueue;

public class CheckersGame {

	//instance variables
	private final Player playerRed;
	private final Player playerWhite;
	private final Player playerActive;
	private final BoardView board;
//	private PriorityQueue<Move> validMoves;

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

		this.playerActive = this.playerRed;

		this.board = new BoardView();
//		validMoves = new PriorityQueue<>();
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

//	public void clearValidMoves()
//	{
//		validMoves = new PriorityQueue<>();
//	}

	// TODO: fix this return?
	public Piece.Color getPlayerColor(Player currentPlayer){
		if(currentPlayer.equals(playerRed)){
			return Piece.Color.RED;
		}
		else if (currentPlayer.equals(playerWhite)){
			return Piece.Color.WHITE;
		}
		else{
			return null;
		}
	}

	public BoardView getBoard() {
		return board;
	}

//	public BoardView getReverseBoard(){
//		return board
//	}
}
