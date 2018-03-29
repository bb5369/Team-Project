package com.webcheckers.appl;

import com.webcheckers.model.Player;

import java.util.HashMap;


/**
 * PlayerLobby manages signed-in players
 */
public class PlayerLobby {

	private static final String MESSAGE_PLAYER_NAME_TAKEN = "Chosen name already exists in Lobby.";
	private static final String MESSAGE_PLAYER_NAME_INVALID = "Chosen player name not valid. Please use only alphanumerics and spaces.";

	// instance variable
	private HashMap<String, Player> activePlayers;

	/**
	 * default constructor
	 * This initializes the activePlayers HashMap on instantiation
	 */
	public PlayerLobby() {
		activePlayers = new HashMap<String, Player>();
	}

	// Behaviors
	/**
	 * getActivePlayers method
	 * This may be an interim method.
	 * @return Hashmap containing all the active players
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
	 * isPlayerInLobby method
	 * Returns true/false if a player value object is in the lobby
	 * @param player
	 * @return
	 */
	public boolean isPlayerInLobby(Player player) {
		if (getActivePlayerCount() == 0) {
			return false;
		}

		if (player == null) {
			return false;
		}

		return activePlayers.containsKey(player.getName());
	}


	/**
	 * newPlayer method
	 * Create a new Player given a name and returns that player
	 * @param name of the new player
	 * @return a new Player
	 */
	public Player newPlayer(String name) {

		if (! isValidName(name)) {
			throw new PlayerLobbyException(MESSAGE_PLAYER_NAME_INVALID);
		}

		if (activePlayers.containsKey(name)) {
			throw new PlayerLobbyException(MESSAGE_PLAYER_NAME_TAKEN);
		}

		final Player newPlayer = new Player(name);

		// Add the player to our lobby
		this.activePlayers.put(name, newPlayer);

		return newPlayer;
	}

	/**
	 * getPlayer method
	 * This method returns a player of
	 * @param name
	 * @return
	 */
	public Player getPlayer(String name) {
			return activePlayers.get(name);
	}


	/**
	 * Validates alphanumeric property of a candidate name for a player
	 * TODO: refactor out of PlayerLobby into util
	 * @param String candidateName
	 * @return Boolean
	 */
	public Boolean isValidName(String candidateName) {
		return candidateName.matches("[a-zA-Z0-9 ]+");
	}


	public void clearLobby() {
		this.activePlayers.clear();
	}


}
