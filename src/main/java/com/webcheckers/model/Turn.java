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

    // Purposely mismatching frontend
    // If a Turn is SUBMITTED then this Turn instance will no longer exist
    public enum State {
        EMPTY_TURN,
        STABLE_TURN
    }

    private Space[][] startingBoard;
    private Player player;
    private Piece.Color playerColor;
    private Stack<Space[][]> pendingMoves;
    private State state;
    private boolean single, mulitJump;
    private int jumps;


    /**
     * Parameterized constructor
     * A turn is identified by the player, the board they are playing on and their color
     *
     * @param startingBoard - The checkers board matrix
     * @param player - player the Turn is being made for
     * @param color - The color of the player's pieces
     */
    Turn(Space[][] startingBoard, Player player, Piece.Color color) {
        LOG.info(String.format("I am a new turn for Player [%s]", player.getName()));

        this.startingBoard = startingBoard;
        this.player = player;
        this.playerColor = color;

        pendingMoves = new Stack<>();
        single = false;
        mulitJump = false;
        jumps = 0;
        state = State.EMPTY_TURN;

        LOG.fine(String.format("Turn initialized in [%s] state", this.state));
    }

    /**
     * Validate an incoming move and add it to this turn's list of moves if its valid
     *
     * @param move - move being validated
     * @return - true if move was validated, otherwise false
     */
    public boolean validateMove(Move move) {
        LOG.info(String.format("%s Player [%s] is validating move %s",
                playerColor,
                player.getName(),
                move.toString()));
        boolean hasJumped = false;
        boolean forcedJump = MoveValidator.forcedJump(getLatestBoard(), move);
        if(jumps > 0)
            hasJumped = true;
        if(move.isSingleSpace() && (hasJumped || forcedJump)){
            return false;
        }
        // TODO: run this through with the team. Not sure if best approach, but allowed for making
        // move validator fully static
        move.setPieceColor(playerColor);
        move.setPlayer(player);

        Space[][] board = getLatestBoard();

        LOG.finest("The board we are using for this validateMove()");
        LOG.finest(CheckersBoardBuilder.formatBoardString(board));

        if (!single && MoveValidator.validateMove(board, move)) {
            LOG.finer("Move has been validated successfully");

            // Now that the move has been validated, lets clone the board and execute the move
            Space[][] newBoard = CheckersBoardBuilder.cloneBoard(board);
            makeMove(newBoard, move);

            pendingMoves.push(newBoard);

            state = State.STABLE_TURN;

            if(move.isSingleSpace()) {
                LOG.finer("This is a single-space move. No more moves can be validated.");
                single = true;
            }
            if(move.isJump()){
                LOG.finer("This is a jump move");
                mulitJump = MoveValidator.isJumpMove(getLatestBoard(), move.getEnd(), move.getPieceColor());
                jumps++;
            }
            LOG.info(String.format("%s Player [%s] has %d queued moves in [%s] state",
                    playerColor,
                    player.getName(),
                    pendingMoves.size(),
                    state));

            return true;

        } else {
            LOG.info("Move was not valid");
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
    public boolean makeMove(Space[][] matrix, Move move){
                LOG.info(String.format("%s Player [%s] turn - executing move %s",
                        playerColor,
                        player.getName(),
                        move.toString()));

                Position start = move.getStart();
                Position end = move.getEnd();

                if (move.isJump()) {
                    jumps++;
                    Space startSpace = matrix[start.getRow()][start.getCell()];
                    Space endSpace = matrix[end.getRow()][end.getCell()];
                    Position midPos = Position.midPosition(end, start);
                    Space midSpace = matrix[midPos.getRow()][midPos.getCell()];
                    //mulitJump = MoveValidator.isJumpMove(matrix, move);

                    return endSpace.jumpPieceMove(startSpace, midSpace);

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

            LOG.info(String.format("Removing last move from %s's history",
                    player.getName()));

            // Return Turn state to EMPTY_TURN if they have no pending moves
            if (pendingMoves.isEmpty()) {
                this.state = State.EMPTY_TURN;
                jumps = 0;
                single = false;
                mulitJump = false;
                LOG.finest(String.format("%s has reversed all planned moves", player.getName()));
                state = State.EMPTY_TURN;
            }
            if(jumps > 0)
                jumps--;
            mulitJump = false;
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

    public Space[][] getLatestBoard() {
        return (pendingMoves.empty()) ? startingBoard : pendingMoves.peek();
    }

    public boolean mulitJumpDone(){
        LOG.fine(String.format("canMultiJump: %b", mulitJump));
        return !mulitJump;
    }
}