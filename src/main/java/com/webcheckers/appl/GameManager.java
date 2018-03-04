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

	public GameManager() {

	}

	/**
	 * Determines whether or not the player is in a game
	 * @param player: player that the method checks if ingame
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

	public CheckersGame getGame(Player playerRed, Player playerWhite) {
		final CheckersGame newGame = new CheckersGame(playerRed, playerWhite);
		gameList.add(newGame);
		return newGame;
	}
}