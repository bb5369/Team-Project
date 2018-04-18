package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Objects;
import java.util.logging.Logger;

public class PostEndSpectateRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());
    private final GameManager gameManager;

    /**
     * Initializes the GetSignOutRoute
     *
     * @param gameManager - game manager used to access games
     */
    PostEndSpectateRoute(final GameManager gameManager) {
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
        LOG.fine("PostEndSpectateRoute invoked");
        Player spectator = request.session().attribute("Player");

        gameManager.removeSpectator(spectator);
        request.session().attribute("message", new Message(String.format("Spectating mode has ended." +
                " <a href=" + WebServer.HOME_URL + ">Return to lobby</a>."), Message.MessageType.info));
                response.redirect(WebServer.HOME_URL);

        return null;
    }
}
