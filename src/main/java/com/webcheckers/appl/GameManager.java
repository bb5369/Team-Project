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

	/**
	 * Determines whether or not the player is in a game
	 * @param player: player that the method checks if in game
	 * @return: true if player is ingame, false if not
	 */
	public boolean isPlayerInGame(Player player){
		for (CheckersGame game : gameList) {
			if(player.equals(game.getPlayerRed()) || player.equals(game.getPlayerWhite())){
				return true;
			}
		}
		return false;
	}

	public CheckersGame getActiveGame(Player player) {
		// TODO: write this
		return new CheckersGame(player, player);
	}

	public CheckersGame getNewGame(Player playerRed, Player playerWhite) {
		final CheckersGame newGame = new CheckersGame(playerRed, playerWhite);
		gameList.add(newGame);
		return newGame;
	}
}