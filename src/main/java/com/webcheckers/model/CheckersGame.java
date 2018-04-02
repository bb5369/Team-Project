package com.webcheckers.model;

public class CheckersGame {

    //instance variables
    private final Player playerRed;
    private final Player playerWhite;
    private Space[][] matrix;
    private Turn activeTurn;



    // to remove
    private final Player resignedPlayer;

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


        generateStartingBoard();

        this.activeTurn = new Turn(this, matrix, playerRed);

        // to remove
        this.resignedPlayer = null;
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
     * Used to access player whose turn it is
     *
     * @return - player whose turn it is
     */
    public Player getPlayerActive() {
        return this.activeTurn.getPlayer();
    }

    /**
     * Changes the player who is active from red to white or vice versa
     */
    private void changeActivePlayer() {
        Player activePlayer = activeTurn.getPlayer();

        if (activePlayer.equals(playerRed)) {
            activeTurn = new Turn(this, matrix, playerWhite);

        } else if (activePlayer.equals(playerWhite)) {
        	activeTurn = new Turn(this, matrix, playerRed);
        }
    }

    /**
     * Advance the game to the next turn using the new player
     */
	 public void nextTurn() {
        // makes sure Turn is SUBMITTED state
        // create new turn with other player

        if (activeTurn.isSubmitted()) {
            changeActivePlayer();
        }
    }


    /**
     * Get the active turn from the game
     * @return
     */
    public Turn getTurn() {
	     return activeTurn;
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
