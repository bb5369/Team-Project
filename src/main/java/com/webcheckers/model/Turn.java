package com.webcheckers.model;

import java.util.Stack;
import java.util.logging.Logger;
import static com.webcheckers.model.CheckersBoardHelper.getSpace;

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
    public enum State{
        EMPTY_TURN,
        SINGLE_MOVE,
        JUMP_MOVE,
        SUBMITTED
    }

    private Space[][] startingBoard;
    private Player player;
    private Piece.Color playerColor;
    private Stack<Space[][]> pendingMoves;
    private State state;


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
        state = State.EMPTY_TURN;

        LOG.fine(String.format("Turn initialized in [%s] state", state));
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

        move.setPieceColor(playerColor);
        move.setPlayer(player);

        Space[][] board = getLatestBoard();

        LOG.finest("The board we are using for this validateMove()");
        LOG.finest(CheckersBoardBuilder.formatBoardString(board));

        boolean isMoveValid = false;

        switch (state) {
            case EMPTY_TURN:
                if (move.isSingleSpace() && MoveValidator.areJumpsAvailableForPlayer(board, playerColor)) {
                	LOG.finer("The player has a jump move available, they must take it");
                    isMoveValid = false;
                } else {
                	isMoveValid = MoveValidator.validateMove(board, move);
				}
                break;

            case SINGLE_MOVE:
                LOG.finer("This is a single-space move. No more moves can be validated.");
                isMoveValid = false;
                break;

            case JUMP_MOVE:
                if (move.isSingleSpace()) {
					isMoveValid = false;
				} else {
                	isMoveValid = MoveValidator.validateMove(board, move);
                }
                break;
        }

        if (isMoveValid) {
        	// The final test is if we can execute the move on the board
        	isMoveValid = recordMove(move);
		}

        LOG.info(String.format("Move validity was %s", isMoveValid));
		LOG.finest(String.format("%s Player [%s] has %d queued moves in [%s] state",
				playerColor,
				player.getName(),
				pendingMoves.size(),
				state));

		return isMoveValid;

    }


    /**
     * This method make moves pieces on the board
     *
     * @param move   - move to be made on the checkers board
     * @return - true if the pieces where moved successfully
     */
    public boolean recordMove(Move move){
		LOG.info(String.format("%s Player [%s] turn - executing move %s",
				playerColor,
				player.getName(),
				move.toString()));

		Space[][] matrix = CheckersBoardBuilder.cloneBoard(getLatestBoard());

		Position start = move.getStart();
		Position end = move.getEnd();

		boolean moveSuccess;

		if (move.isJump()) {
			// remove the piece at the midpoint
			Space jumpedSpace = getSpace(matrix, move.getMidpoint());
			moveSuccess = jumpedSpace.removePiece() == Space.State.OPEN;

		} else {
			Space startSpace = getSpace(matrix, move.getStart());
			Space endSpace = getSpace(matrix, move.getEnd());

			moveSuccess = endSpace.movePieceFrom(startSpace);
		}

		if (moveSuccess) {
			LOG.finest("Move successfully made on board");
		    pendingMoves.push(matrix);
		    setStateAfterMove(move);
        }

        return moveSuccess;
    }

    private void setStateAfterMove(Move move) {
        if (move.isSingleSpace()) {
            state = State.SINGLE_MOVE;
        } else if (move.isJump()) {
        	state = State.JUMP_MOVE;
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
            pendingMoves.pop();

            LOG.info(String.format("Removing last move from %s's history",
                    player.getName()));

            // Return Turn state to EMPTY_TURN if they have no pending moves
            if (pendingMoves.isEmpty()) {
                state = State.EMPTY_TURN;
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
        switch (state) {
            case SINGLE_MOVE:
            case JUMP_MOVE:
                return true;

            default:
                return false;
        }
    }

    public boolean canResign(){
        return state == State.EMPTY_TURN;
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
}
