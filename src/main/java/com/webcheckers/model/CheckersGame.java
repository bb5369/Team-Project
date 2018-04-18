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
        this.winner = null;
        this.loser = null;

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
        if(activeTurn == null)
            return winner;
        return this.activeTurn.getPlayer();
    }

    /**
     * Changes the player who is active from red to white or vice versa
	 * This is a private method that assumes the Turn is over
     */
    private void changeActivePlayer() {
        Player activePlayer = activeTurn.getPlayer();
        Piece.Color activePlayerColor = getPlayerColor(activePlayer);
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

        makeKings();

        boolean nextPlayerHasPieces = MoveValidator.playerHasPieces(board, nextPlayerColor);
        boolean nextPlayerHasMoves  = MoveValidator.areMovesAvailableForPlayer(board, nextPlayer, nextPlayerColor);

        boolean isActivePlayerOutOfMoves = ! MoveValidator.areMovesAvailableForPlayer(board, activePlayer, activePlayerColor);


        if (!nextPlayerHasPieces) {
            LOG.fine(String.format("WON: %s won, %s lost. (%s has no pieces)", activePlayer, nextPlayer, nextPlayer));
        	recordEndGame(activePlayer, nextPlayer);

        } else if (nextPlayerHasPieces && isActivePlayerOutOfMoves) {
            LOG.fine(String.format("WON: %s won, %s lost. (%s put themselves in a corner)", nextPlayer, activePlayer, activePlayer));
        	recordEndGame(nextPlayer, activePlayer);

        } else if (nextPlayerHasMoves && nextPlayerHasPieces) {
        	LOG.fine("Nobody has WON yet");
            activeTurn = new Turn(board, nextPlayer, nextPlayerColor);
        }
    }

    /**
     * Transition game into WON state, recording winner and loser status
     * @param winner
     * @param loser
     */
    private void recordEndGame(Player winner, Player loser) {
    	state = State.WON;

        this.winner = winner;
        this.loser = loser;

        winner.wonAGame();

        //if (winner.isTournament()) {
            TournamentScoreboard.sortPlayers();
        //}

        activeTurn = null;

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
        if (playerRed.getName().equals("Tester")) {
            try {
                Method boardBuilderMethod = TestCheckersBoards.class.getMethod(playerWhite.getName());

                builder = (CheckersBoardBuilder) boardBuilderMethod.invoke(null);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                builder = CheckersBoardBuilder.aStartingBoard();
            }
        } else {
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

    /**
     * When pieces reach the proper end row, the piece will be kinged
     */
    private void makeKings(){
        // King red pieces
        for(int cell = 0; cell < 8; cell++){
            if(board[0][cell].isOccupied() && board[0][cell].getPiece().getColor() == Piece.Color.RED){
                board[0][cell].getPiece().kingMe();
            }
        }

        // King white pieces
        for(int cell = 0; cell < 8; cell++){
            if(board[7][cell].isOccupied() && board[7][cell].getPiece().getColor() == Piece.Color.WHITE){
                board[7][cell].getPiece().kingMe();
            }
        }
    }

    /**
     * Allow the active player to resign the game
     * @return boolean - if resignation was successful
     */
    public boolean resignGame(Player player) {
        if (activeTurn == null)
            return false;

        if (!player.equals(activeTurn.getPlayer()) || activeTurn.canResign()) {
            state = State.RESIGNED;
            loser = player;
            if(playerRed.equals(player))
                winner = playerWhite;
            else
                winner = playerRed;
            this.activeTurn = null;
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

    @Override
    public String toString(){
        return this.getPlayerRed().getName() + " vs. " + this.getPlayerWhite().getName();
    }
}
