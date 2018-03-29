package com.webcheckers.appl;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;

import java.util.PriorityQueue;
import java.util.logging.Logger;

/**
 * Turn controller handles the lifecycle of a player's turn
 * From validating moves
 *   to backing up those moves
 *   to submitting a list of moves and completing their turn
 * The turn controller is an expert at it all!
 */
public class TurnController {
	private static final Logger LOG = Logger.getLogger(TurnController.class.getName());

	private CheckersGame game;
	private Player player;
	private MoveValidator moveValidator;

	private PriorityQueue<Move> validMoves;


	/** Parameterized constructor
	 * A turn is identified by a game and a player
	 * @param game
	 * @param player
	 */
	TurnController(CheckersGame game, Player player) {
		this.game = game;
		this.player = player;

		validMoves = new PriorityQueue<>();

		moveValidator = new MoveValidator(game, player);

		LOG.fine(String.format("TurnController initialized for Player [%s]", player.getName()));
	}

	/**
	 * Validate an incoming move and add it to this turn's list of moves if its valid
	 * @param move
	 * @return
	 */
	public boolean validateMove(Move move) {

		if (isMyTurn() && moveValidator.validateMove(move)) {
			LOG.finest("Adding valid move to players turn history");

			validMoves.add(move);

			LOG.finest(String.format("%s Player [%s] has %d queued moves",
					game.getPlayerColor(player),
					player.getName(),
					validMoves.size()));

			return true;
		}

		return false;
	}


	/**
	 * When planning a turn a player may wish to backup and try a new strategy
	 * We will remove their last valid move from the queued moves
	 */
	public void backupMove() {
		validMoves. // TODO: validate if this actually pulls the last valid move pushed
	}

	/**
	 * Is the player who owns this object the active player in the game?
	 * @return boolean if player is active player
	 */
	public boolean isMyTurn() {
		return game.getPlayerActive().equals(player);
	}

}
