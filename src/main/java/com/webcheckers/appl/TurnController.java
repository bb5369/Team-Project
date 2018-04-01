package com.webcheckers.appl;

import com.webcheckers.model.*;
import com.webcheckers.util.DoublyLinkedQueue;

import java.util.PriorityQueue;
import java.util.logging.Logger;

/**
 * Turn controller handles the lifecycle of a player's turn
 * From validating moves
 * to backing up those moves
 * to submitting a list of moves and completing their turn
 * The turn controller is an expert at it all!
 */
public class TurnController {
    //instance variables
    private static final Logger LOG = Logger.getLogger(TurnController.class.getName());
    private CheckersGame game;
    private Player player;
    private MoveValidator moveValidator;
    private DoublyLinkedQueue<Move> validMoves;


    /**
     * Parameterized constructor
     * A turn is identified by a game and a player
     *
     * @param game   - game the TurnController is being made for
     * @param player - player the TurnController is being made for
     */
    TurnController(CheckersGame game, Player player) {
        this.game = game;
        this.player = player;

        validMoves = new DoublyLinkedQueue<>();

        moveValidator = new MoveValidator(game, player);

        LOG.fine(String.format("TurnController initialized for Player [%s]", player.getName()));
    }

    /**
     * Validate an incoming move and add it to this turn's list of moves if its valid
     *
     * @param move - move being validated
     * @return - true if move was validated, otherwise false
     */
    public boolean validateMove(Move move) {

        if (isMyTurn() && moveValidator.validateMove(move)) {
            LOG.finest("Adding valid move to players turn history");

            validMoves.enqueue(move);

            LOG.finest(String.format("%s Player [%s] has %d queued moves",
                    game.getPlayerColor(player),
                    player.getName(),
                    validMoves.size()));

            return true;
        }

        return false;
    }

    /**
     * This method implement the submitMove logic
     * making the necessary changes to the board
     *
     * @return - true if the submitMove was successful, false otherwise
     */
    public boolean submitTurn() {
        //get the list of moves;
        Space[][] matrix = game.getMatrix();
        while (!validMoves.isEmpty())//goes through all the valid  moves
        {
            if (!makeMove(matrix, validMoves.removeFromFront())) {
                return false;
            }
        }
        game.changeActivePlayer();
        return true;
    }

    /**
     * This method make moves pieces on the board
     *
     * @param matrix - represents the checkers board
     * @param move   - move to be made on the checkers board
     * @return - true if the pieces where moved successfully
     */
    public boolean makeMove(Space[][] matrix, Move move) {
        //get the location
        //get the piece
        //move the piece to the location
        // clear the piece in the path if it is a capture move
        Position start = move.getStart();
        Position end = move.getEnd();

        if (move.isAJumpMoveAttempt()) {
            //TODO jump move logic goes here
        } else //single move logic
        {
            Space startSpace = matrix[start.getRow()][start.getCell()];
            Space endSpace = matrix[end.getRow()][end.getCell()];

            return endSpace.movePieceFrom(startSpace);
        }


        return true;
    }


    /**
     * When planning a turn a player may wish to backup and try a new strategy
     * We will remove their last valid move from the queued moves
     *
     * @return - true if there are valid moves, false otherwise
     */
    public boolean backupMove() {
        if (!validMoves.isEmpty()) {
            validMoves.removeFromRear(); // TODO: validate if this actually pulls the last valid move pushed
        } else {
            return false;
        }
        return true;
    }

    /**
     * Is the player who owns this object the active player in the game?
     *
     * @return - true if player is active player
     */
    public boolean isMyTurn() {
        return game.getPlayerActive().equals(player);
    }

}
