package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    private final TemplateEngine    templateEngine;
    private final PlayerLobby       playerLobby;
    private final GameManager       gameManager;

    private final String VIEW_TITLE = "Checkers Game";

    private final String VIEW_NAME = "game.ftl";

    private static String PLAYER_IN_GAME_MESSAGE = "The player you've selected is already in a game.";
    private static String PLAYER_NOT_EXIST_MESSAGE = "The player by that name does not exist";
    private static String PLAYER_INVALID_SELECT = "You cannot play a game with yourself";
    private static String PLAYER_LEFT_GAME = "The other player is not in the game";

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
	 * @param playerLobby
     *   Player Lobby component
     * @param gameManager
     *   Game Manager component
     */
    public GetGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(playerLobby, "gameManager must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;

        LOG.config("GetHomeRoute is initialized.");
    }


    /**
     * Spark Controller for rendering new and existing games
     *
     * @param request
     * @param response
     * @return Rendered template engine
     */
    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("GetGameRoute is invoked");

		final Player currentPlayer = request.session().attribute("Player");

		if (currentPlayer != null && gameManager.isPlayerInAnyGame(currentPlayer)) {
			if(gameManager.isPlayerInAResignedGame(currentPlayer)) {
				//CheckersGame game = gameManager.getResignedGame(currentPlayer);
				gameManager.clearGame(currentPlayer);
				gameManager.clearResigned(currentPlayer);
				if(!gameManager.isPlayerInAResignedGame(currentPlayer))
					redirectWithType(request, response, new Message(PLAYER_LEFT_GAME, Message.MessageType.info), WebServer.HOME_URL);
				else
					response.redirect(WebServer.HOME_URL);
				halt();
			}
			else{
				return renderGame(currentPlayer, null);//playGameWith(currentPlayer);
			}

			//return renderGame(game, currentPlayer);
		}
			else if (currentPlayer != null && haveParam(request,"whitePlayer")) {
			// We are setting up a new game

			// NOTE: The player initiating the game will ALWAYS be the red player, therefore the opponent is white
			final Player redPlayer = currentPlayer;
			final Player whitePlayer = playerLobby.getPlayer(request.queryParams("whitePlayer"));

			if (redPlayer.equals(whitePlayer)) {
				redirectWithType(request, response, new Message(PLAYER_INVALID_SELECT, Message.MessageType.error), WebServer.HOME_URL);
			}

			if (whitePlayer == null) {
				redirectWithType(request, response, new Message(PLAYER_NOT_EXIST_MESSAGE, Message.MessageType.error), WebServer.HOME_URL);
			}

			if (gameManager.isPlayerInAGame(redPlayer) || gameManager.isPlayerInAGame(whitePlayer)) {
				redirectWithType(request, response, new Message(PLAYER_IN_GAME_MESSAGE, Message.MessageType.error), WebServer.HOME_URL);
			}

			return renderGame(redPlayer, whitePlayer);//playGameBetween(redPlayer, whitePlayer);
		} else {
			response.redirect(WebServer.HOME_URL);
			halt();
		}

		// We shouldn't ever hit this, but Spark redirects are unclean so this is a catch-all until a better design
		// is proposed.
		LOG.warning("We fell through in GameRoute...no view available");
		return templateEngine.render(new ModelAndView(new HashMap<String, Object>(), "home.ftl"));
   }


	/**
	 * Helper function to determine if the given Spark request has a named parameter
	 * @param request
	 * @param paramName
	 * @return true/false
	 */
	private boolean haveParam(Request request, String paramName) {
		final String param = request.queryParams(paramName);

		return param != null;
	}

	/**
	 * Helper function used to redirect to a route and show the user an message
	 * @param request
	 * @param response
	 * @param message
	 * @param destination
	 */
	private void redirectWithType(Request request, Response response, Message message, String destination) {
		LOG.fine(String.format("Redirecting to %s with %s [%s]", destination, message.getType(), message.getText()));

		request.session().attribute("message", message);
		response.redirect(destination);
	}

	/**
	 * Render a given checkers game from the perspective of the session player
	 * @param sessionPlayer
	 * @return
	 */
	private Object renderGame(Player sessionPlayer, Player opponentPlayer) {
		CheckersGame game;
			if(opponentPlayer == null)
				game = gameManager.getGame(sessionPlayer);
			else
				game = gameManager.getGame(sessionPlayer, opponentPlayer);
		return templateEngine.render(new ModelAndView(gameManager.renderGame(game, sessionPlayer, VIEW_TITLE), VIEW_NAME));
	}
}

