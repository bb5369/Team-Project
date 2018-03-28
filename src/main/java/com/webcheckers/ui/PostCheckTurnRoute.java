package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import spark.TemplateEngine;
import spark.Response;
import spark.Route;
import spark.Request;

import java.util.Objects;
import java.util.logging.Logger;

public class PostCheckTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private final TemplateEngine templateEngine;

    private final GameManager gameManager;
    private final PlayerLobby playerLobby;

    public PostCheckTurnRoute(TemplateEngine templateEngine, PlayerLobby playerLobby, GameManager gameManager) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "player must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;

        LOG.config("PostCheckTurnRoute is initialized");
    }

    @Override
    public Object handle(Request request, Response response){
        LOG.finer("PostCheckTurnRoute is invoked.");

        Player currrentPlayer = request.session().attribute("Player");

        CheckersGame game = gameManager.getGame(currrentPlayer);

        if(game != null && (game.getPlayerRed().equals(null) || game.getPlayerWhite().equals(null))){
            response.body("Opponent resigned");
            gameManager.clearGame(currrentPlayer);
            response.redirect(WebServer.HOME_URL);
        }
        else if (game == null)
            response.redirect(WebServer.HOME_URL);
        else
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
