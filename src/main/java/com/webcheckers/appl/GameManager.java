package com.webcheckers.appl;

import java.util.*;
import java.util.logging.Logger;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import com.webcheckers.model.Turn;

/**
 * Coordinates the state of active games across the entire application
 */
public class GameManager {

    private static final Logger LOG = Logger.getLogger(GameManager.class.getName());

    // A list of all active games
    private ArrayList<CheckersGame> gameList;
    private HashMap<Player, Player> spectators;

    /**
     * default construct
     * Initializes gameList on instantiation
     */
    public GameManager() {
        gameList = new ArrayList<>();
        spectators = new HashMap<>();
    }


    /**
     * Determines whether or not a given player is in a game
     *
     * @param player - player that the method checks if in game
     * @return - true if player is ingame, false if not
     */
    public boolean isPlayerInAGame(Player player) {

        if (gameList == null || player == null) return false;

        for (CheckersGame game : gameList) {
            if (isPlayerInThisGame(game, player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerASpectator(Player player){
        return spectators.containsKey(player);
    }


    public void addSpectator(Player spectator, Player player){
        spectators.put(spectator, player);
    }

    public void removeSpectator(Player player){
        spectators.remove(player);
    }

    public CheckersGame getSpectatorGame(Player player){
        return this.getGame(spectators.get(player));
    }

    public HashMap<String, CheckersGame> getGameList(){
        HashMap<String, CheckersGame> games = new HashMap<>();
        for (CheckersGame game : gameList) {
            games.put(game.toString(), game);
        }
        return games;
    }
    /**
     * This is a private method that checks if the player is in a game
     *
     * @param game   - game to check in
     * @param player - player to check for
     * @return - true if the player is in the game
     */
    private boolean isPlayerInThisGame(CheckersGame game, Player player) {
        return player.equals(game.getPlayerRed()) || player.equals(game.getPlayerWhite());
    }


    /**
     * Get a game between two players - Existing or New game
     * This is used to fetch the game that player is currently in
     * If there isn't a game, it will make one
     *
     * @param player1 - the first player
     * @param player2 - the second player
     * @return - CheckerGame reference to the game that the player is in
     */
    public CheckersGame getGame(Player player1, Player player2) {
        CheckersGame aGame = null;

        for (CheckersGame game : gameList) {
            if (isPlayerInThisGame(game, player1) && isPlayerInThisGame(game, player2)) {
                LOG.finer(String.format("getGame(Player: '%s', Player: '%s') Found a game in progress",
                        player1.getName(),
                        player2.getName()));

                return game;
            }
        }

        return getNewGame(player1, player2);
    }


    /**
     * Get the game that a given player is currently in
     *
     * @param currentPlayer - player being inspected
     * @return - CheckersGame that the player is in
     */
    public CheckersGame getGame(Player currentPlayer) {
        for (CheckersGame game : gameList) {
            if (isPlayerInThisGame(game, currentPlayer)) {
                LOG.finer(String.format("getGame(Player: '%s') Found game in STATE %s",
                        currentPlayer.getName(),
                        game.getState()));
                return game;
            }
        }

        return null;
    }


    /**
     * getNewGame method
     * This method is used to creating and adding a new CheckerGame
     * into the gameList
     *
     * @param playerRed   - player 1
     * @param playerWhite - player 2
     * @return - reference to newly created CheckerGame game
     */
    public CheckersGame getNewGame(Player playerRed, Player playerWhite) {

        if (isPlayerInAGame(playerRed) || isPlayerInAGame(playerWhite)) {
            LOG.warning(String.format("getNewGame(Player: '%s', Player: '%s') Player in requested pair already in game",
                    playerRed.getName(),
                    playerWhite.getName()));

            return null;
        }

        final CheckersGame newGame = new CheckersGame(playerRed, playerWhite);
        gameList.add(newGame);

        LOG.fine(String.format("getNewGame(Player: '%s', Player: '%s') New game created",
                playerRed.getName(),
                playerWhite.getName()));

        return newGame;
    }

    public boolean resignGame(Player player) {
        CheckersGame game = getGame(player);

        // A player can only resign if it their turn and they have not made any moves
        if (game.resignGame(player)) {
        	return true;

        } else {
            return false;
        }
    }

    public void clearGame(Player player) {
        gameList.remove(getGame(player));
    }

    public void clearGames() {
        this.gameList.clear();
    }

    /**
     * Get the Turn of the given player
     *
     * @param player - player whose game a Turn is being made for
     * @return - a new Turn
     */
    public Turn getPlayerTurn(Player player) {
        final CheckersGame game = getGame(player);

        if (player.equals(game.getPlayerActive())) {
            return game.getTurn();
        }

        return null;

    }

    /**
     * Removes a game from the active games list
     *
     * @param game - game being removed
     */
    public void destoryGame(CheckersGame game) {
        this.gameList.remove(game);

    }
}
