package com.webcheckers.model;

import java.util.Stack;
import java.util.logging.Logger;

/**
 * Turn handles the lifecycle of a player's turn
 * From validating moves
 * to backing up those moves
 * to submitting a list of moves and completing their turn
 * The turn controller is an expert at it all!
 */
public class Turn {
    private static final Logger LOG = Logger.getLogger(Turn.class.getName());

    public enum State {
        EMPTY_TURN,
        STABLE_TURN,
        TURN_SUBMITTED
    }

    private CheckersGame game;
    private MoveValidator moveValidator;
    private Space[][] startingBoard;
    private Player player;
    private Stack<Space[][]> pendingMoves;
    private State state;
    private boolean single;


    /**
     * Parameterized constructor
     * A turn is identified by a game and a player
     *
     * @param game   - game the Turn is being made for
     * @param startingBoard - The checkers board matrix
     * @param player - player the Turn is being made for
     */
    Turn(CheckersGame game, Space[][] startingBoard, Player player) {
        LOG.fine(String.format("I am a new turn for Player [%s]", player.getName()));

        this.game = game;
        this.startingBoard = startingBoard;
        this.player = player;
        this.pendingMoves = new Stack<>();
        this.single = false;
        this.state = State.EMPTY_TURN;

        this.moveValidator = new MoveValidator(player, game.getPlayerColor(player));

        LOG.fine(String.format("Turn initialized in [%s] state", this.state));
    }

    /**
     * Validate an incoming move and add it to this turn's list of moves if its valid
     *
     * @param move - move being validated
     * @return - true if move was validated, otherwise false
     */
    public boolean validateMove(Move move) {
        LOG.finer(String.format("%s Player [%s] is validing move %s",
                game.getPlayerColor(player),
                player.getName(),
                move.toString()));

        // TODO: pull out to own function
        // This determines the board we are going to validate the move against
        Space[][] board = (pendingMoves.empty()) ? startingBoard : pendingMoves.peek();

        LOG.finest("The board we are using in validateMove()");
        LOG.finest(CheckersBoardBuilder.formatBoardString(board));

        if (!single && moveValidator.validateMove(board, move)) {
            LOG.finer("Move has been validated successfully");

            //Clones the board on top of the stack, and creates a new board with the move executed, which is pushed
            Space[][] newBoard = CheckersBoardBuilder.cloneBoard(board);
            makeMove(newBoard, move);

            pendingMoves.push(newBoard);

            state = State.STABLE_TURN;

            if(move.isASingleMoveAttempt()) {
                LOG.finer("This is a single-space move");
                single = true;
            }

            LOG.finest(String.format("%s Player [%s] has %d queued moves in [%s] state",
                    game.getPlayerColor(player),
                    player.getName(),
                    pendingMoves.size(),
                    state));

            return true;

        } else {
            LOG.finer("Move is not valid");
            return false;

        }
    }

    /**
     * This method make moves pieces on the board
     *
     * @param matrix - represents the checkers board
     * @param move   - move to be made on the checkers board
     * @return - true if the pieces where moved successfully
     */
    public boolean makeMove(Space[][] matrix, Move move) {
        LOG.finer(String.format("%s Player [%s] turn - executing move %s",
                game.getPlayerColor(player),
                player.getName(),
                move.toString()));

        Position start = move.getStart();
        Position end = move.getEnd();

        if (move.isAJumpMoveAttempt()) {
            //TODO jump move logic goes here
            return true;

        } else {
            Space startSpace = matrix[start.getRow()][start.getCell()];
            Space endSpace = matrix[end.getRow()][end.getCell()];
            single = true;

            return endSpace.movePieceFrom(startSpace);
        }
    }


    /**
     * When planning a turn a player may wish to backup and try a new strategy
     * We will remove their last valid move from the queued moves
     *
     * @return - true if there are valid moves, false otherwise
     */
    public boolean backupMove() {
        if (!pendingMoves.isEmpty()) {
            Space[][] badMove = pendingMoves.pop();

            LOG.finest(String.format("Removing move %s from %s's history",
                    badMove.toString(),
                    player.getName()));

            // Return Turn state to EMPTY_TURN if they have no pending moves
            if (pendingMoves.isEmpty()) {
                this.state = State.EMPTY_TURN;
            }
            single = false;
            return true;
        }

        return false;
    }

    /**
     * Is the given player in an active turn?
     *
     * @return - true if player is active player
     */
    public boolean isMyTurn(Player player) {
        return this.player.equals(player);
    }

    /**
     * Returns whether or not the turn has been submitted
     *
     * @return - true if the turn has been submitted, false otherwise
     */
    public boolean isSubmitted() {
        return (this.state == State.TURN_SUBMITTED);
    }

    /**
     * A stable turn is one where a player has made one or more moves
     * @return
     */
    public boolean isStable() {
        return state == State.STABLE_TURN;
    }

    public boolean canResign(){
        return this.state == State.EMPTY_TURN;
    }


    /**
     * Return the player whose turn it is
     *
     * @return - the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Used in testing to inspect component state
     * @return Turn State
     */
    public State getState() {
        return this.state;
    }

    public Space[][] getFinalBoard() {
        state = State.TURN_SUBMITTED;

        return pendingMoves.peek();
    }
}