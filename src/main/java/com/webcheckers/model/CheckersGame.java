package com.webcheckers.model;

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

        activeTurn = new Turn(board, nextPlayer, nextPlayerColor);
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
        CheckersBoardBuilder builder = CheckersBoardBuilder.aBoard();

        switch (playerRed.getName()) {
            case "noMoreMoves":
                builder.withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.WHITE),
                        new Position(1, 0)
                ).withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.WHITE),
                        new Position(1, 2)
                ).withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.WHITE),
                        new Position(0, 3)
                ).withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.RED),
                        new Position(3, 0)
                );
                break;

            case "endgame":
                builder.withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.WHITE),
                        new Position(1, 0)
                ).withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.RED),
                        new Position(2, 1)
                );
                break;

            case "noPieces":
                builder.withPieceAt(
                        new Piece(Piece.Type.SINGLE, Piece.Color.RED),
                        new Position(1, 0)
                );

            default:
                // I don't actually have a public method to place starting pieces.. otherwise that woudl go here
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
	 * Allow the active player to submit their turn
     * @param player
     * @return
     */
    public boolean submitTurn(Player player) {
        if (player.equals(getPlayerActive()) && activeTurn.isStable()) {
            board = activeTurn.getLatestBoard();

            changeActivePlayer();

            winCases();

            return true;
        }

        return false;
    }

    public void winCases(){
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
