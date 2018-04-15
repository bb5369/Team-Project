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

    public enum MoveState{
        JUMP_MOVE,
        MULT_JUMP_MOVE,
        SINGLE_MOVE,
        NO_MOVE,
        MOVE_DONE;
    }

    private Space[][] startingBoard;
    private Player player;
    private Piece.Color playerColor;
    private Stack<Space[][]> pendingMoves;
    private State state;
    private MoveState moveState;


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
        moveState = MoveState.NO_MOVE;
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
        updateMove(move, false);
        if(move.isSingleSpace() && (MoveState.JUMP_MOVE == moveState || MoveState.MULT_JUMP_MOVE == moveState ||
                                                                    MoveValidator.forcedJump(getLatestBoard(), move))){
            return false;
        }
        // TODO: run this through with the team. Not sure if best approach, but allowed for making
        // move validator fully static
        move.setPieceColor(playerColor);
        move.setPlayer(player);

        Space[][] board = getLatestBoard();

        LOG.finest("The board we are using for this validateMove()");
        LOG.finest(CheckersBoardBuilder.formatBoardString(board));

        if (moveState != MoveState.MOVE_DONE && MoveValidator.validateMove(board, move)) {
            LOG.finer("Move has been validated successfully");

            // Now that the move has been validated, lets clone the board and execute the move
            Space[][] newBoard = CheckersBoardBuilder.cloneBoard(board);
            makeMove(newBoard, move);

            pendingMoves.push(newBoard);

            state = State.STABLE_TURN;

            if(move.isSingleSpace()) {
                LOG.finer("This is a single-space move. No more moves can be validated.");
            }
            if(move.isJump()){
                LOG.finer("This is a jump move");
            }
            LOG.info(String.format("%s Player [%s] has %d queued moves in [%s] state in [%s] moveState",
                    playerColor,
                    player.getName(),
                    pendingMoves.size(),
                    state, moveState));
            updateMove(move, true);
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
                    Space startSpace = matrix[start.getRow()][start.getCell()];
                    Space endSpace = matrix[end.getRow()][end.getCell()];
                    Position midPos = Position.midPosition(end, start);
                    Space midSpace = matrix[midPos.getRow()][midPos.getCell()];
                    //mulitJump = MoveValidator.isJumpMove(matrix, move);

                    return endSpace.jumpPieceMove(startSpace, midSpace);

                    } else {
                        Space startSpace = matrix[start.getRow()][start.getCell()];
                        Space endSpace = matrix[end.getRow()][end.getCell()];
                        updateMove(move, true);

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
                this.moveState = MoveState.NO_MOVE;
                LOG.finest(String.format("%s has reversed all planned moves", player.getName()));
            }
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

    private void updateMove(Move move, boolean checkEnd){
        if(state == State.EMPTY_TURN)
            this.moveState = MoveState.NO_MOVE;
        if(moveState == MoveState.SINGLE_MOVE)
            this.moveState = MoveState.MOVE_DONE;
        if(move.isSingleSpace() && moveState == MoveState.NO_MOVE)
            this.moveState = MoveState.SINGLE_MOVE;
        if(moveState == MoveState.MULT_JUMP_MOVE)
            if(checkEnd && MoveValidator.isJumpMove(getLatestBoard(), move.getStart(), move.getPieceColor()))
                this.moveState = MoveState.MOVE_DONE;
        if(moveState == MoveState.JUMP_MOVE)
            if(MoveValidator.isJumpMove(getLatestBoard(), move.getEnd(), move.getPieceColor()))
                this.moveState = MoveState.MULT_JUMP_MOVE;
            else
                this.moveState = MoveState.MOVE_DONE;
        if(moveState == MoveState.NO_MOVE && move.isJump())
            this.moveState = MoveState.JUMP_MOVE;
    }

    public Space[][] getLatestBoard() {
        return (pendingMoves.empty()) ? startingBoard : pendingMoves.peek();
    }

    public boolean mulitJumpDone(){
        return MoveState.MOVE_DONE == moveState;
    }
}