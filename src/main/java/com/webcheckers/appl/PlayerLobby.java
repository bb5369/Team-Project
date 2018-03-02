package com.webcheckers.appl;

import java.util.HashMap;

import com.webcheckers.model.Player;

/**
 * PlayerLobby manages signed-in players
 */
public class PlayerLobby {

	// Attributes
	private HashMap<String, Player> activePlayers;

	public PlayerLobby() {
		activePlayers = new HashMap<String, Player>();
	}

	// Behaviors
	/**
	 * getActivePlayers method
	 * This may be an interim method.
	 * @return Hashmap containing all the active playes
	 */
	public HashMap<String, Player> getActivePlayers() {
		return activePlayers;
	}

	/**
	 * getActivePlayerCount method
	 * Returns a count of the number of active players in the lobby
	 * @return Integer activePlayerCount
	 */
	public Integer getActivePlayerCount() {
		return this.activePlayers.size();
	}

	/**
	 * newPlayer method
	 * Create a new Player given a name and returns that player
	 * @param name of the new player
	 * @return a new Player
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
