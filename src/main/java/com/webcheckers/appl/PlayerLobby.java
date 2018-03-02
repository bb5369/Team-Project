package com.webcheckers.appl;

import java.util.HashMap;

import com.webcheckers.model.Player;

/**
 * PlayerLobby manages signed-in players
 */
public class PlayerLobby {

	// Attributes
	private HashMap<String, Player> activePlayers = new HashMap<String, Player>();

	public PlayerLobby() {
	}


	/**
	 * This may be an interim method.
	 * @return
	 */
	public HashMap<String, Player> getActivePlayers() {
		return activePlayers;
	}

	/**
	 * Returns a count of the number of active players in the lobby
	 * @return Integer activePlayerCount
	 */
	public Integer getActivePlayerCount() {
		return this.activePlayers.size();
	}

	/**
	 * Create a new Player given a name and returns that player
	 * @param name
	 * @return Player
	 */
	public Player newPlayer(String name) {
		final Player newPlayer = new Player(name);

		// TODO: check for invalid characters in player name
		// TODO: check hashmap if name exists, bail if so

		// Add the player to our lobby
		this.activePlayers.put(name, newPlayer);

		return newPlayer;
	}
}
