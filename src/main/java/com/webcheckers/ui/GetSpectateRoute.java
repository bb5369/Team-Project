package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.logging.Logger;

public class  GetSpectateRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;

    /**
     * Initializes the GetSignOutRoute
     *
     * @param playerLobby
     * @param gameManager - game manager used to access games
     */
    GetSpectateRoute(final PlayerLobby playerLobby, final GameManager gameManager) {
        Objects.requireNonNull(gameManager, "Game Manager must not be null");
        Objects.requireNonNull(playerLobby, "Player Lobby must not be null");

        this.gameManager = gameManager;
        this.playerLobby = playerLobby;
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
        LOG.fine("GetSpectateRoute invoked");
        Player spectator = request.session().attribute("Player");
        Player gamePlayer = playerLobby.getPlayer(request.queryParams("redPlayer"));

        LOG.fine("Adding Spectator");
        gameManager.addSpectator(spectator, gamePlayer);

        LOG.fine("Redircting to game");
        response.redirect(WebServer.GAME_URL);

        return null;
    }
}
