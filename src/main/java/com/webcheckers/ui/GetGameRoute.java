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

public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    private final TemplateEngine    templateEngine;
    private final PlayerLobby       playerLobby;
    private final GameManager       gameManager;

    private final String VIEW_TITLE = "Checkers Game";

    private final String VIEW_NAME = "game.ftl";

    private static String PLAYER_IN_GAME_MESSAGE = "The player you've selected is already in a game.";
    private static String PLAYER_NOT_EXIST_MESSAGE = "The player by that name does not exist";

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

		if (currentPlayer != null && gameManager.isPlayerInAGame(currentPlayer)) {
			// We are playing an existing game
			if(!gameManager.getGame(currentPlayer).getOtherPlayer(currentPlayer).equals(new Player("null")))
				return playGameWith(currentPlayer);
			else {
				gameManager.clearGame(currentPlayer);
				redirectWithError(request, response, "The other player is not in the game", WebServer.HOME_URL);
			}

        } else if (haveParam(request,"whitePlayer")) {
			// We are setting up a new game

			// NOTE: The player initiating the game will ALWAYS be the red player, therefore the opponent is white
			final Player redPlayer = currentPlayer;
			final Player whitePlayer = playerLobby.getPlayer(request.queryParams("whitePlayer"));

			if (redPlayer.equals(whitePlayer)) {
				redirectWithError(request, response, "You cannot play a game with yourself", WebServer.HOME_URL);
			}

			if (whitePlayer == null) {
				redirectWithError(request, response, PLAYER_NOT_EXIST_MESSAGE, WebServer.HOME_URL);
			}

			if (gameManager.isPlayerInAGame(redPlayer) || gameManager.isPlayerInAGame(whitePlayer)) {
				redirectWithError(request, response, PLAYER_IN_GAME_MESSAGE, WebServer.HOME_URL);
			}

			return playGameBetween(redPlayer, whitePlayer);
		} else {
			response.redirect(WebServer.HOME_URL);
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
	 * Helper function used to redirect to a route and show the user an error
	 * @param request
	 * @param response
	 * @param message
	 * @param destination
	 */
	private void redirectWithError(Request request, Response response, String message, String destination) {
		LOG.fine(String.format("Redirecting to %s with error [%s]", destination, message));

		Message messageObj = new Message(message, Message.MessageType.error);
		request.session().attribute("message", messageObj);
		response.redirect(destination);
	}

	private Object playGameBetween(Player currentPlayer, Player opponentPlayer) {
		LOG.fine(String.format("Playing game between [%s] and [%s]", currentPlayer.getName(), opponentPlayer.getName()));

		final CheckersGame game = gameManager.getGame(currentPlayer, opponentPlayer);

		return renderGame(game, currentPlayer);
	}

	/**
	 * Given a player we will render the template for the player's current game
	 * @param currentPlayer
	 * @return Rendered template engine template game.ftl
	 */
	private Object playGameWith(Player currentPlayer) {
		LOG.fine(String.format("Playing game with [%s]", currentPlayer.getName()));

		final CheckersGame game = gameManager.getGame(currentPlayer);

		return renderGame(game, currentPlayer);
	}

	/**
	 * Render a given checkers game from the perspective of the session player
	 * @param game
	 * @param sessionPlayer
	 * @return
	 */
	private Object renderGame(CheckersGame game, Player sessionPlayer) {
		LOG.fine(String.format("Rendering game between red player [%s] and white player [%s]. currentPlayer: [%s]",
				game.getPlayerRed().getName(),
				game.getPlayerWhite().getName(),
				sessionPlayer.getName()));

        Map<String, Object> vm = new HashMap();

		final Player redPlayer = game.getPlayerRed();
		final Player whitePlayer = game.getPlayerWhite();

		vm.put("title", VIEW_TITLE);
		vm.put("currentPlayer", sessionPlayer);
		vm.put("viewMode", "PLAY");
		vm.put("redPlayer", redPlayer);
		vm.put("whitePlayer",whitePlayer);
		vm.put("activeColor", game.getPlayerColor(game.getPlayerActive()));

		if(sessionPlayer.equals(whitePlayer)) {
			vm.put("board", game.getBoard());
		} else {
			vm.put("board", game.getBoard().getReverseBoard());
		}

		return templateEngine.render(new ModelAndView(vm, VIEW_NAME));

	}
}

