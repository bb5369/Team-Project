package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;

public class GetSpectateRoute implements Route {

    private final GameManager gameManager;

    /**
     * Initializes the GetSignOutRoute
     *
     * @param gameManager - game manager used to access games
     */
    GetSpectateRoute(final GameManager gameManager) {
        Objects.requireNonNull(gameManager, "Game Manager must not be null");
        this.gameManager = gameManager;
    }

    /**
     * Removes the player from the player lobby and destroys the game
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - null
     */
    @Override
    public Object handle(Request request, Response response) {
        Player spectator = request.session().attribute("Player");
        String gamePlayer = request.queryParams("redPlayer");
        Player redPlayer = new Player(gamePlayer);

        CheckersGame game = gameManager.getGame(redPlayer);

        gameManager.addSpectator(spectator, redPlayer);

        response.redirect(WebServer.GAME_URL);

        return null;
    }
}
