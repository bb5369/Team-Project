package com.webcheckers.model;

public class CheckersGame {

	//instance variables
	private final Player playerRed;
	private final Player playerWhite;
	private final Player playerActive;
	private final BoardView board;
	private final Player resignedPlayer;

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

		this.resignedPlayer = null;
	}

	public CheckersGame(Player playerRed, Player playerWhite, Player playerActive, BoardView board){
		this.playerRed = playerRed;
		this.playerWhite = playerWhite;

		this.playerActive = playerActive;

		this.board = board;

		this.resignedPlayer = null;
	}

	public CheckersGame(CheckersGame game, Player player){
		this.playerRed = game.getPlayerRed();
		this.playerWhite = game.getPlayerWhite();

		this.playerActive = game.getPlayerActive();

		this.board = game.getBoard();

		this.resignedPlayer = player;
	}
	public Player getPlayerRed() {
		return playerRed;
	}

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
			return new CheckersGame(new Player("null"), game.getPlayerWhite(), active, game.getBoard());
		}
		else {
			if(game.getPlayerActive().equals(player))
				active = player;
			else
				active = game.getPlayerWhite();
			return new CheckersGame(game.getPlayerRed(), new Player("null"), active, game.getBoard());
		}
	}


	// TODO: fix this return?
	public Piece.color getPlayerColor(Player currentPlayer){
		if(currentPlayer.equals(playerRed)){
			return Piece.color.RED;
		}
		else if (currentPlayer.equals(playerWhite)){
			return Piece.color.WHITE;
		}
		else{
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
	public BoardView getBoard() {
		return board;
	}

//	public BoardView getReverseBoard(){
//		return board
//	}
}
