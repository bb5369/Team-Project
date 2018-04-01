package com.webcheckers.model;

public class CheckersGame {

    //instance variables
    private final Player playerRed;
    private final Player playerWhite;
    private final Player resignedPlayer;
    private Player playerActive;
    private Space[][] matrix;

    /**
     * Parameterized constructor
     * Creation of a new checkers game between two players
     *
     * @param playerRed   - Player one
     * @param playerWhite - Player two
     */
    public CheckersGame(Player playerRed, Player playerWhite) {
        this.playerRed = playerRed;
        this.playerWhite = playerWhite;

        this.playerActive = this.playerRed;

        this.resignedPlayer = null;
        generateStartingBoard();
    }

    /**
     * Parameterized constructor
     * Creates a resigned game, given the player resigning and
     * the game being resigned from
     *
     * @param game   - Game being resigned from
     * @param player - Player resigning
     */
    public CheckersGame(CheckersGame game, Player player) {
        this.playerRed = game.getPlayerRed();
        this.playerWhite = game.getPlayerWhite();

        this.resignedPlayer = player;
    }


    /**
     * Space matrix representing a checkers board
     *
     * @return - space matrix
     */
    public Space[][] getMatrix() {
        return matrix;
    }

    /**
     * Used to access the red player in the game
     *
     * @return - Red player in the game
     */
    public Player getPlayerRed() {
        return playerRed;
    }

    /**
     * getWhitePlayer method--
     * Used to access the white player in the game
     *
     * @return - White player in the game
     */
    public Player getPlayerWhite() {
        return playerWhite;
    }

    /**
     * Accesses the opponent player to the player passed in
     *
     * @param player - Player whose opponent is being returned
     * @return - Player not passed in
     */
    public Player getOtherPlayer(Player player) {
        if (playerRed.equals(player)) {
            return playerWhite;
        } else {
            return playerRed;
        }
    }

    /**
     * Used to access player whose turn it is
     *
     * @return - player whose turn it is
     */
    public Player getPlayerActive() {
        return playerActive;
    }

    /**
     * Changes the player who is active from red to white or vice versa
     */
    public void changeActivePlayer() {
        if (playerActive.equals(playerRed)) {
            playerActive = playerWhite;
        } else {
            playerActive = playerRed;
        }
    }

    /**
     * What color is the given player?
     *
     * @param player - Player whose color is being requested
     * @return Piece.Color  - color of the Player
     */
    public Piece.Color getPlayerColor(Player player) {
        if (player.equals(playerRed)) {
            return Piece.Color.RED;
        } else if (player.equals(playerWhite)) {
            return Piece.Color.WHITE;
        } else {
            return null;
        }
    }

    /**
     * Returns whether or not a player has resigned from the game
     *
     * @param player - Player being checked if resigned
     * @return - false if there is no resigned player, true if player is resigned
     */
    public boolean isResignedPlayer(Player player) {
        if (resignedPlayer == null) {
            return false;
        } else {
            return this.resignedPlayer.equals(player);
        }
    }


    /**
     * Uses our static BoardBuilder to generate the starting Checkers Board
     */
    private void generateStartingBoard() {
        this.matrix = BoardBuilder.buildBoard();
    }
}
