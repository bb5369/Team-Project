package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.GameManager;

import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import spark.*;

import java.util.logging.Logger;

/**
 * GetClearRoute is an admin only route that clears the PlayerLobby and GameManager hashmaps
 * <p>
 * To use this route your player name must start with 'admin', case-insensitive
 * <p>
 * initially created to ease testing without restarting the service.
 */
public class GetClearRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetClearRoute.class.getName());


    private final PlayerLobby playerLobby;
    private final GameManager gameManager;

    private static String META_REFRESH_HOME = "<meta http-equiv=\"refresh\" content=\"0;URL='/'\" />";

    /**
     * Initializes GetClearRoute
     *
     * @param playerLobby -
     * @param gameManager -
     */
    GetClearRoute(PlayerLobby playerLobby, GameManager gameManager) {
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
        LOG.finer("GetClearRoute initialized");
    }

    /**
     * If the player is an admin, allow them to clear the games and player lobby
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - the link to clear everything
     */
    @Override
    public Object handle(Request request, Response response) {
        final Player currentPlayer = request.session().attribute("Player");

        if (currentPlayer != null && currentPlayer.getName().toLowerCase().startsWith("admin")) {
            LOG.finer("GetClearRoute invoked by admin");

            LOG.finest("Clearing games...");
            this.gameManager.clearGames();

            LOG.finest("Clear players...");
            this.playerLobby.clearLobby();

            return redirectWithMessage(request, "[ADMIN] You've cleared active games and the lobby!");
        }

        return META_REFRESH_HOME;
    }

    /**
     * Redirects the player to the cleared lobby with a message
     *
     * @param req     - the HTTP request
     * @param message - the message getting passed
     * @return
     */
    private Object redirectWithMessage(Request req, String message) {
        Message messageObj = new Message(message, Message.MessageType.error);
        req.session().attribute("message", messageObj);

        return META_REFRESH_HOME;
    }
}
