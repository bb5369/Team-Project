package com.webcheckers.appl;

import java.util.*;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

/**
 * Coordinates the state of active games across the entire application
 */
public class GameManager {

	//A list containing each active game
	private ArrayList<CheckersGame> gameList;

	public GameManager()
	{
		gameList = new ArrayList<>();
	}

	/**
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

	private boolean playerPlayingThisGame(Player player, CheckersGame game)
	{
		return player.equals(game.getPlayerRed()) || player.equals(game.getPlayerWhite());
	}

	public CheckersGame getActiveGame(Player player) {
		CheckersGame aGame = null;
		for(CheckersGame game: gameList)
		{
			if(playerPlayingThisGame(player, game))
			{
				aGame = game;
				break;
			}
		}
		return aGame;
	}


	public CheckersGame getNewGame(Player playerRed, Player playerWhite) {
		final CheckersGame newGame = new CheckersGame(playerRed, playerWhite);
		gameList.add(newGame);
		return newGame;
	}
}