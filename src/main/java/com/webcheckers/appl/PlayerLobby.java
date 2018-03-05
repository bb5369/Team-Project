package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.HashMap;


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

		if (! isValidName(name)) {
			throw new PlayerLobbyException("Chosen player name not valid. Please use only alphanumerics and spaces.");
		}

		if (activePlayers.containsKey(name)) {
			throw new PlayerLobbyException("Chosen name already exists in Lobby");
		}

		final Player newPlayer = new Player(name);

		// Add the player to our lobby
		this.activePlayers.put(name, newPlayer);

		return newPlayer;
	}

	public Player getPlayer(String name) {
		// TODO: throw exception for nonexistant player
		return activePlayers.get(name);
	}

	// Private methods

	/**
	 * Validates alphanumeric property of a candidate name for a player
	 * TODO: refactor out of PlayerLobby into util
	 * @param String candidateName
	 * @return Boolean
	 */
	public Boolean isValidName(String candidateName) {
		return candidateName.matches("[a-zA-Z0-9 ]+");
	}
}
