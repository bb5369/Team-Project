package com.webcheckers.appl;

import java.util.*;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;

/**
 * Coordinates the state of active games across the entire application
 */
public class GameManager {

	public GameManager() {

	}

	public CheckersGame newGame(Player playerRed, Player playerWhite) {
		final CheckersGame newGame = new CheckersGame(playerRed, playerWhite);

		return newGame;
	}



}
