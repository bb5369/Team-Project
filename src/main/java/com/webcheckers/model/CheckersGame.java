package com.webcheckers.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class CheckersGame {
    private static final Logger LOG = Logger.getLogger(CheckersGame.class.getName());

    protected enum State {
        IN_PLAY,
        WON,
        RESIGNED
    }

    private final Player playerRed;
    private final Player playerWhite;
    private Player winner;
    private Player loser;
    private Space[][] board;
    private Turn activeTurn;
    private State state;

    /**
     * Parameterized constructor
     * Create a new checkers game between two players
     *
     * @param playerRed   - Player one, red player, starting player
     * @param playerWhite - Player two
     */
    public CheckersGame(Player playerRed, Player playerWhite) {
        LOG.info(String.format("I am a new CheckersGame between [%s] and [%s]",
                playerRed.getName(),
                playerWhite.getName()));

        this.playerRed = playerRed;
        this.playerWhite = playerWhite;
        this.state = State.IN_PLAY;

        initStartingBoard();

        this.activeTurn = new Turn(board, playerRed, Piece.Color.RED);
    }


    // PLAYER INTERFACE

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
	 * This is a private method that assumes the Turn is over
     */
    private void changeActivePlayer() {
        Player activePlayer = activeTurn.getPlayer();
        Player nextPlayer;
        Piece.Color nextPlayerColor;

        // determine who the next player is
		if (activePlayer.equals(playerWhite)) {
		    nextPlayer = playerRed;
		    nextPlayerColor = Piece.Color.RED;
        } else {
            nextPlayer = playerWhite;
            nextPlayerColor = Piece.Color.WHITE;
        }

        // make sure the next player has moves available
        if (MoveValidator.areMovesAvailableForPlayer(board, nextPlayer, nextPlayerColor)) {
            // setup their turn
            activeTurn = new Turn(board, nextPlayer, nextPlayerColor);
        } else {
		    // trigger a win for activePlayer
            LOG.info(String.format("%s has no more moves. Sad! %s wins.", nextPlayer.getName(), activePlayer.getName()));
            winCases();
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


    // BOARD INTERFACE

    /**
     * Two-dimensional Space array representing a Checkers board
     *
     * @return - space board
     */
    public Space[][] getBoard() {
        return board;
    }

    /**
     * Uses our static CheckersBoardBuilder to generate the starting Checkers Board
     */
    private void initStartingBoard() {
        CheckersBoardBuilder builder;

        // Paging Mike Rowe - we've got a dirty job
        // This is our backdoor into setting up a starting board for testing
        // If the red player is named one of the public static methods in TestCheckersBoards
        // then we use that board builder
        try {
            Method boardBuilderMethod = TestCheckersBoards.class.getMethod(playerRed.getName());

            builder = (CheckersBoardBuilder)boardBuilderMethod.invoke(null);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)  {
            System.out.println(e.toString());
            e.printStackTrace();
            builder = CheckersBoardBuilder.aStartingBoard();
        }

        LOG.finest("Starting board:");
        LOG.finest(builder.formatBoardString());

        board = builder.getBoard();
    }


    // TURN INTERFACE

    /**
     * Get the active turn from the game
     * @return - the active turn
     */
    public Turn getTurn() {
        return activeTurn;
    }

    /**
	 * Allow the active player to submit their turn if preconditions have been met
     * Preconditions are:
     *   1. Turn is finalized (no more jump moves, at least one move queued)
     * @param player
     * @return Message indicating reason for turn submission
     */
    public Message submitTurn(Player player) {
        if (player.equals(getPlayerActive())) {
        	Message finalizedMessage = getTurn().isFinalized();
        	if (finalizedMessage.getType() == Message.MessageType.info) {
                board = getTurn().getLatestBoard();
                changeActivePlayer();
            }
            return finalizedMessage;

        } else {
            return new Message("It is not your turn", Message.MessageType.error);
        }
    }

    private void winCases(){
        Player inactivePlayer;
        if(getPlayerColor(getPlayerActive()) == getPlayerColor(playerRed))
            inactivePlayer = playerWhite;
        else
            inactivePlayer = playerRed;
        // If activePlayer has no moves or pieces left, they lose
        if(!MoveValidator.areMovesAvailableForPlayer(board, getPlayerActive(), getPlayerColor(getPlayerActive())) ||
                !MoveValidator.playerHasPieces(board, getPlayerActive(), getPlayerColor(getPlayerActive()))) {
            LOG.info(String.format("%s has no more moves. Sad! %s wins.", getPlayerActive().getName(), inactivePlayer.getName()));
            this.winner = inactivePlayer;
            this.loser = getPlayerActive();
            this.state = State.WON;
        }
    }

    /**
     * Allow the active player to resign the game
     * @return boolean - if resignation was successful
     */
    public boolean resignGame() {
        if (activeTurn.canResign()) {
            state = State.RESIGNED;

            return true;
        }

        return false;
    }

    /**
     * Indicates if this game is resigned
     * @return boolean
     */
    public boolean isResigned() {
        return state == State.RESIGNED;
    }

    /**
     * Indicates if this game has been won
     * @return - true if the game is won, false otherwise
     */
    public boolean isWon(){ return state == State.WON; }

    /**
     * Used for logging
     * @return
     */
    public State getState() {
        return state;
    }

    /**
     * Returns the winner of the game
     *
     * @return - the winner
     */
    public Player getWinner(){ return this.winner; }

    /**
     * Returns the loser of the game
     *
     * @return - the loser
     */
    public Player getLoser(){ return this.loser; }
}
