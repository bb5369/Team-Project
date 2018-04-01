package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

/**
 * UI Controller to GET the sign out route
 */
public class GetSignOutRoute implements Route {

    private final PlayerLobby playerLobby;
    private final GameManager gameManager;

    /**
     */
    GetSignOutRoute(final PlayerLobby playerLobby, final GameManager gameManager){
        Objects.requireNonNull(playerLobby, "Player Lobby must not be null");
        Objects.requireNonNull(gameManager, "Game Manager must not be null");
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
    }

    @Override
    public Object handle(Request request, Response response) {
        Player player = request.session().attribute("Player");
        String playerName = player.getName();

        CheckersGame game = gameManager.getGame(player);

        playerLobby.destroyPlayer(playerName);

        if ( ! playerLobby.isPlayerInLobby(player)) {
            // Remove the player from the session
            request.session().removeAttribute(playerName);
            player = null;
            gameManager.destoryGame(game);
            game = null;
        }
        // Redirect to homepage which should show the Signed Out page
        response.redirect(WebServer.HOME_URL);

        return null;
    }
}
