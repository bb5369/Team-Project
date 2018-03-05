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

public class GetGameRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;

    private final String VIEW_NAME = "home.ftl";

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(playerLobby, "gameManager must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
        LOG.config("GetHomeRoute is initialized.");
    }
    @Override
    public Object handle(Request request, Response response){
        LOG.finer("GetGameRoute is invoked");

        Map<String, Object> vm = new HashMap();

        if (request.session().attribute("Player") != null) {
            final Player currentPlayer = request.session().attribute("Player");
            if(gameManager.isPlayerInGame(currentPlayer)){
                //System.out.println("redPlayer: " + redPlayerName + " whitePlayer: " + whitePlayerName);

                // make sure we have an ?opponent (This should never happen
                if (currentPlayer.getName() == null) {
                    // TODO: indicate to user why this happened
                    response.redirect(WebServer.HOME_URL);
                }
                final CheckersGame game = gameManager.getActiveGame(currentPlayer);
                // get opponent player model
                final Player redPlayer = game.getPlayerRed();
                final Player whitePlayer = game.getPlayerWhite();


//        // get my player model
//		final Player sessionPlayer = request.session().attribute("Player");

                // check to see if either of us are in a game
                    vm.put("title", WebServer.GAME_URL);
                    vm.put("currentPlayer", game.getPlayerColor(currentPlayer));
                    vm.put("viewMode", WebServer.GAME_URL);
                    vm.put("redPlayer", redPlayer);
                    vm.put("whitePlayer",whitePlayer);
                    vm.put("activeColor", Piece.color.RED);
                    vm.put("board", game.getBoard());

                    return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
                }

                // get a new game with our models

                // Direct player at /game
            }
            return null;
    }
}


