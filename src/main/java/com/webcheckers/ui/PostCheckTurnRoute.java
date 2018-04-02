package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import spark.TemplateEngine;
import spark.Response;
import spark.Route;
import spark.Request;

import com.google.gson.Gson;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Checks if it's a player's turn or not
 */
public class PostCheckTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());

    private final GameManager gameManager;

    private final String opponentResigned = "true"/*"Your opponent has resigned From the game"*/;

    private final Gson gson;

    /**
     * Initializes the PostCheckTurnRoute
     *
     * @param gameManager - used to find a game
     * @param gson        - used to transmit messages to AJAX
     */
    public PostCheckTurnRoute(GameManager gameManager, Gson gson) {
        // validation
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        Objects.requireNonNull(gson, "gson must not be null");

        this.gameManager = gameManager;
        this.gson = gson;

        LOG.config("PostCheckTurnRoute is initialized");
    }

    /**
     * Determines if a player is resigning
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - null or a Json message
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostCheckTurnRoute is invoked.");

        Player currentPlayer = request.session().attribute("Player");
        CheckersGame game;
        if (gameManager.isPlayerInAResignedGame(currentPlayer))
            game = gameManager.getResignedGame(currentPlayer);
        else
            game = null;
        if (game != null) {
            Message message = new Message(opponentResigned, Message.MessageType.info);
            gameManager.clearGame(currentPlayer);
            gameManager.clearResigned(currentPlayer);
            if (game.isResignedPlayer(currentPlayer))
                response.redirect(WebServer.HOME_URL);
            else
                return formatMessageJson(message);
        }
        return null;
    }

    /**
     * formatMessageJson - Format text and a message type as JSON for use in returning to the frontend
     *
     * @return - gson Message object
     */
    public Object formatMessageJson(Message message) {
        return gson.toJson(message);
    }
}

