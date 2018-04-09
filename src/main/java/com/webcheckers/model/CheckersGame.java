package com.webcheckers.model;

public class CheckersGame {

    protected enum State {
        IN_PLAY,
        WON,
        RESIGNED
    }

    //instance variables
    private final Player playerRed;
    private final Player playerWhite;
    private Space[][] matrix;
    private Turn activeTurn;
    private State state;


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
        this.state = State.IN_PLAY;

        generateStartingBoard();

        this.activeTurn = new Turn(this, matrix, playerRed);
    }

    /**
     * Space matrix representing a checkers board
     *
     * @return - space matrix
     */
    public Space[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Space[][] matrix)
    {
        this.matrix = matrix;
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
     * @return - the active turn
     */
    public Turn getTurn() {
	     return activeTurn;
    }

    public boolean resignGame() {

        if (activeTurn.canResign()) {
            state = State.RESIGNED;

            return true;
        }

        return false;
    }

    public boolean isResigned() {
        return state == State.RESIGNED;
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

    public Player getOtherPlayer(Player player){
        if(player.equals(playerRed))
            return playerRed;
        else
            return playerWhite;
    }

    /**
     * Used for logging
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * Uses our static BoardBuilder to generate the starting Checkers Board
     */
    private void generateStartingBoard() {
        this.matrix = BoardBuilder.buildBoard();
    }

}
