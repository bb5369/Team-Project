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
     * This may be an interim method.
     *
     * @return - Hashmap containing all the active players
     */
    public HashMap<String, Player> getActivePlayers() {
        return activePlayers;
    }

    /**
     * Returns a count of the number of active players in the lobby
     *
     * @return - Integer activePlayerCount
     */
    public Integer getActivePlayerCount() {
        return this.activePlayers.size();
    }

    /**
     * Returns true/false if a player value object is in the lobby
     *
     * @param player - player being checked
     * @return - returns true if the player is in the lobby, false otherwise
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
     * Create a new Player given a name and returns that player
     *
     * @param name - name of the new player
     * @return - a new Player
     */
    public Player newPlayer(String name, Player.GameType type) {

        if (!isValidName(name)) {
            throw new PlayerLobbyException(MESSAGE_PLAYER_NAME_INVALID);
        }

        if (activePlayers.containsKey(name)) {
            throw new PlayerLobbyException(MESSAGE_PLAYER_NAME_TAKEN);
        }

        final Player newPlayer = new Player(name, type);

        // Add the player to our lobby
        this.activePlayers.put(name, newPlayer);

        return newPlayer;
    }

    /**
     * Returns an active player
     *
     * @param name - Name of the player being searched for
     * @return - Player being searched for
     */
    public Player getPlayer(String name) {
        return activePlayers.get(name);
    }


    /**
     * Validates alphanumeric property of a candidate name for a player
     * TODO: refactor out of PlayerLobby into com.webcheckers.util
     *
     * @param candidateName - name attempting to be entered
     * @return - true if it is a valid name, false otherwise
     */
    public Boolean isValidName(String candidateName) {
        return candidateName.matches("[a-zA-Z0-9 ]+");
    }

    /**
     * Clears the list of active players
     */
    public void clearLobby() {
        this.activePlayers.clear();
    }

    /**
     * Remove the given player from the map of active players
     *
     * @param playerName - Player being removed
     */
    public void destroyPlayer(String playerName) {
        this.activePlayers.remove(playerName);
    }
}
