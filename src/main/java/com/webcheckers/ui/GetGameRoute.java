package com.webcheckers.ui;

import com.webcheckers.appl.BoardViewGen;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;
import static spark.Spark.redirect;

/**
 * Create the Spark Route (UI controller) for the
 * {@code GET /} HTTP request.
 */
public class GetGameRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;

    private final String VIEW_TITLE = "Checkers Game";

    private final String VIEW_NAME = "game.ftl";
    private String viewMode, redirect;

    private static String PLAYER_IN_GAME_MESSAGE = "The player you've selected is already in a game.";
    private static String PLAYER_NOT_EXIST_MESSAGE = "The player by that name does not exist";
    private static String PLAYER_INVALID_SELECT = "You cannot play a game with yourself";
    private static String PLAYER_LEFT_GAME = "The other player is not in the game";
    private static String PLAYERS_DIFFERENT_TYPES = "You cannot play a game with someone in a different mode.";

    /**
     * Initializes the GetGameRoute
     *
     * @param templateEngine - the HTML template rendering engine
     * @param playerLobby    - Player Lobby component
     * @param gameManager    - Game Manager component
     */
    public GetGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(playerLobby, "gameManager must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
        this.viewMode = "PLAY";
        this.redirect = WebServer.HOME_URL;

        LOG.config("GetHomeRoute is initialized.");
    }


    /**
     * Spark Controller for rendering new and existing games
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return Rendered template engine
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetGameRoute is invoked.");

        final Player currentPlayer = request.session().attribute("Player");

        Map<String, Object> vm = new HashMap();

        // If a message is available, display it.
        if (request.session().attribute("message") != null) {
            final Message messageToRender = request.session().attribute("message");
            request.session().removeAttribute("message");
            vm.put("message", messageToRender);
        }
        // TODO: Refactor the conditional game set-up logic below into GameManager
        if (currentPlayer != null && (gameManager.isPlayerASpectator(currentPlayer) || gameManager.isPlayerInAGame(currentPlayer))){
                if (gameManager.isPlayerInAGame(currentPlayer)){
                    viewMode = "PLAY";
                    redirect = WebServer.HOME_URL;
                    return renderGame(vm, currentPlayer, null);
                }
                else{
                    viewMode = "SPECTATOR";
                    redirect = WebServer.ENDSPECTATE_URL;
                    CheckersGame game = gameManager.getSpectatorGame(currentPlayer);
                    LOG.fine(String.format("Rendering a Spectator for: %s",game.toString()));
                    return renderGame(vm, game.getPlayerRed(), game.getPlayerWhite());
                }

        } else if (currentPlayer != null && haveParam(request, "whitePlayer")) {
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

            if (gameManager.isPlayerInAGame(redPlayer) || gameManager.isPlayerInAGame(whitePlayer) || gameManager.isPlayerInAGame(currentPlayer)) {
                redirectWithType(request, response, new Message(PLAYER_IN_GAME_MESSAGE, Message.MessageType.error), WebServer.HOME_URL);
            }
            if (redPlayer.getType() != whitePlayer.getType()){
                redirectWithType(request, response, new Message(PLAYERS_DIFFERENT_TYPES, Message.MessageType.error), WebServer.HOME_URL);
            }

            return renderGame(vm, redPlayer, whitePlayer);
        } else {
            response.redirect(WebServer.HOME_URL);
            //halt();
            return null;
        }

        // We shouldn't ever hit this, but Spark redirects are unclean so this is a catch-all until a better design
        // is proposed.
        //LOG.warning("We fell through in GameRoute...no view available");
        //return templateEngine.render(new ModelAndView(new HashMap<String, Object>(), "home.ftl"));
    }


    /**
     * Helper function to determine if the given Spark request has a named parameter
     *
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
     *
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
     *
     * @param sessionPlayer
     * @return
     */
    private Object renderGame(Map<String, Object> vm, Player sessionPlayer, Player opponentPlayer) {
        CheckersGame game;
        if (opponentPlayer == null) {
            LOG.fine(String.format("Playing game with [%s]", sessionPlayer.getName()));
            game = gameManager.getGame(sessionPlayer);
        } else {
            LOG.fine(String.format("Playing game between [%s] and [%s]", sessionPlayer.getName(), opponentPlayer.getName()));
            game = gameManager.getGame(sessionPlayer, opponentPlayer);
        }

        if (game.isWon()) {
            if (vm.get("message") == null) {
                vm.put("message", new Message(String.format("Game won by %s", game.getWinner().getName()), Message.MessageType.info));
            }
        }
        if(game.isResigned()){
            if (vm.get("message") == null) {
                if(VIEW_NAME == "PLAY") {
                    vm.put("message", new Message(String.format("%s has resigned, %s has won the game <a href='/'>return to lobby</a>.",
                            game.getLoser().getName(), game.getWinner().getName()), Message.MessageType.info));
                }
                else{
                    vm.put("message", new Message(String.format("%s has resigned, %s has won the game. Click the Exit Spectate button to return to the lobby",
                            game.getLoser().getName(), game.getWinner().getName()), Message.MessageType.info));
                }
            }
        }
        return templateEngine.render(new ModelAndView(renderGame(game, sessionPlayer, vm), VIEW_NAME));
    }

    /**
     * renderGame method
     * This method renders the game
     * @param game - game object
     * @param sessionPlayer - the player session object
     * @param vm - A map of key value pairs need for rendering the game
     * @return A map with all the key value pair needed to render the game
     */
    public Map<String, Object> renderGame(CheckersGame game, Player sessionPlayer, Map<String, Object> vm) {
        LOG.fine(String.format("Rendering game between red player [%s] and white player [%s]. currentPlayer: [%s]",
                game.getPlayerRed().getName(),
                game.getPlayerWhite().getName(),
                sessionPlayer.getName()));

        final Player redPlayer = game.getPlayerRed();
        final Player whitePlayer = game.getPlayerWhite();

        vm.put("title", VIEW_TITLE);
        vm.put("currentPlayer", sessionPlayer);
        vm.put("viewMode", viewMode);
        vm.put("redPlayer", redPlayer);
        vm.put("whitePlayer", whitePlayer);
        vm.put("activeColor", game.getPlayerColor(game.getPlayerActive()));

        //Generates a board with the stored matrix in the instance of CheckersGame for the view
        BoardViewGen board = new BoardViewGen(game.getBoard());

        if (sessionPlayer.equals(redPlayer)) {
            vm.put("board", board);
        } else {
            vm.put("board", board.getReverseBoard());
        }

        // This is a really bad place to put something as important as this
	    // Now that we've rendered the game for the final time, get rid of it!
        if (game.isResigned() || (game.isWon())) {
            gameManager.clearGame(sessionPlayer);
        }

        return vm;
    }
}