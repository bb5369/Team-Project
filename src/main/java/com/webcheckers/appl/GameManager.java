package com.webcheckers.appl;

import java.util.*;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

/**
 * Coordinates the state of active games across the entire application
 */
public class GameManager {

	//A list active players
	private ArrayList<CheckersGame> gameList;

	/**
	 * default construct
	 * Initializes gameList on instantiation
	 */
	public GameManager()
	{
		gameList = new ArrayList<>();
	}

	/**
	 * isPlayerInGame
	 * Determines whether or not the player is in a game
	 * @param player: player that the method checks if in game
	 * @return: true if player is ingame, false if not
	 */
	public boolean isPlayerInGame(Player player){
//		if (player.getName() == "iamtesting") return true;

		if (gameList == null || player == null ) return false;

		for (CheckersGame game : gameList) {
			if(playerPlayingThisGame(player, game)){
				return true;
			}
		}
		return false;
	}

	/**
	 * playerPlayingThisGame
	 * This is a private method that checks if the player is in a game
	 * @param player - player to check for
	 * @param game - game to checkin
	 * @return true if the player is in the game
	 */
	private boolean playerPlayingThisGame(Player player, CheckersGame game)
	{
		return player.equals(game.getPlayerRed()) || player.equals(game.getPlayerWhite());
	}

	/**
	 * getActiveGame method
	 * This is used to fetch the game that player is currently in
	 * If there isn't a game, it will make one
	 * @param player1 - the first player
	 * @param player2 - the second player
	 * @return - CheckerGame reference to the game that the player is in
	 */
	public CheckersGame getActiveGame(Player player1, Player player2) {
		CheckersGame aGame = null;
		for(CheckersGame game: gameList)
		{
			if(playerPlayingThisGame(player1, game))
			{
				aGame = game;
				break;
			}
		}
		if(aGame == null){
			aGame = getNewGame(player1, player2);
		}
		return aGame;
	}

	/**
	 * getNewGame method
	 * This method is used to creating and adding a new CheckerGame
	 * into the gameList
	 * @param playerRed - player 1
	 * @param playerWhite - player 2
	 * @return reference to newly created CheckerGame game
	 */
	public CheckersGame getNewGame(Player playerRed, Player playerWhite) {
		final CheckersGame newGame = new CheckersGame(playerRed, playerWhite);
		gameList.add(newGame);
		return newGame;
	}
}