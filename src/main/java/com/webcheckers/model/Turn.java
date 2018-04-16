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

    public static final String NO_MOVES_MSG          = "You have not made any moves this turn.";
    public static final String JUMP_MOVE_AVAIL_MSG   = "You have a jump move available and must take it.";
    public static final String JUMP_MOVE_PARTIAL_MSG = "You can continue your jump.";
    public static final String SINGLE_MOVE_ONLY_MSG  = "You can only make one single move in a turn.";

    public static final String VALID_SINGLE_MOVE_MSG = "Valid single move! Submit your turn.";
    public static final String VALID_JUMP_MOVE_MSG   = "Valid jump move!";


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
    private Move lastValidMove;


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
	 * @return - Message - to be used in the UI to indicate to the player if their move was successful
	 * or why not.
     */
    public Message validateMove(Move move) {
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
        Message moveValidMessage = new Message("Move is invalid.", Message.MessageType.error);

        // TODO: Handle the logic of each case in a seperate method
        switch (state) {
            case EMPTY_TURN:
                if (move.isSingleSpace() && MoveValidator.areJumpsAvailableForPlayer(board, playerColor)) {
                	moveValidMessage = new Message(JUMP_MOVE_AVAIL_MSG, Message.MessageType.error);
                } else if (MoveValidator.validateMove(board, move)) {
					String message = (move.isSingleSpace()) ? VALID_SINGLE_MOVE_MSG : VALID_JUMP_MOVE_MSG;
					moveValidMessage = new Message(message, Message.MessageType.info);
				}
                break;

            case SINGLE_MOVE:
            	// If they have made a single move, they cannot make another
                moveValidMessage = new Message(SINGLE_MOVE_ONLY_MSG, Message.MessageType.error);
                break;

            case JUMP_MOVE:
                if (move.isSingleSpace()) {
                	moveValidMessage =  new Message(JUMP_MOVE_PARTIAL_MSG, Message.MessageType.error);
				} else if (MoveValidator.validateMove(board, move)) {
                	moveValidMessage = new Message(VALID_JUMP_MOVE_MSG, Message.MessageType.info);
                }
                break;
        }

        if ( moveValidMessage.getType() == Message.MessageType.info) {
        	// The final test is if we can execute the move on the board
        	recordMove(move);
		}

		LOG.info(String.format("Move Validated. Result [%s]", moveValidMessage.toString()));

		LOG.finest(String.format("%s Player [%s] has %d queued moves in [%s] state",
				playerColor,
				player.getName(),
				pendingMoves.size(),
				state));

		return moveValidMessage;

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

		if (move.isJump()) {
			// remove the piece at the midpoint
			Space jumpedSpace = getSpace(matrix, move.getMidpoint());
			jumpedSpace.removePiece();
		}

		Space startSpace = getSpace(matrix, move.getStart());
		Space endSpace = getSpace(matrix, move.getEnd());

		if (endSpace.movePieceFrom(startSpace)) {
			LOG.finest("Move successfully made on board");

		    pendingMoves.push(matrix);
		    lastValidMove = move;

		    setStateAfterMove(move);

		    return true;
        } else {
			return false;
		}
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
	 * Determine if the Turn can be submitted
	 * A "finalized" turn requires:
	 *   1. A move to have been made
	 *   2. Multi-jump to be completed fully
	 * @return
	 */
	public Message isFinalized() {
		Message finalizedMessage = new Message("Turn has been finalized", Message.MessageType.info);
		switch (state) {
			case SINGLE_MOVE:
				return finalizedMessage;

			case JUMP_MOVE:
				if (MoveValidator.canContinueJump(getLatestBoard(), lastValidMove.getEnd(), playerColor)) {
					return new Message(JUMP_MOVE_PARTIAL_MSG, Message.MessageType.error);
				} else {
					return finalizedMessage;
				}

			default:
				return new Message(NO_MOVES_MSG, Message.MessageType.error);
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
