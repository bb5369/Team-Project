package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * UI controller to POSt when a game is resigned
 */
public class PostResignGame implements Route {
    private static final Logger LOG = Logger.getLogger(PostResignGame.class.getName());

    private final GameManager gameManager;
    private final String infoMessage = "Resign the current player from the game";
    private final String errorMessage = "The current play cannot resign from the game";
    private final Gson gson;

    /**
     * Initializes PostResignGame
     *
     * @param gameManager - used to resign from the game
     * @param gson        - used to pass a message to AJAX
     */
    public PostResignGame(GameManager gameManager, final Gson gson) {
        // validation
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.gameManager = gameManager;
        this.gson = gson;
        LOG.config("PostResignRoute is initialized");
    }

    /**
     * Resigns the game
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - Json message
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGame is invoked.");

        Player currentPlayer = request.session().attribute("Player");
        CheckersGame game = gameManager.getGame(currentPlayer);
        if(false && !game.getTurn().isMyTurn(currentPlayer) || (game.getTurn().isMyTurn(currentPlayer) && !game.getTurn().isSubmitted())) {
            gameManager.resignGame(currentPlayer);

            // TODO: If we can't resign from the game then return that message
            return formatMessageJson(Message.MessageType.info, infoMessage);
        }
        else{
            return formatMessageJson(Message.MessageType.error, errorMessage);
        }
    }

    /**
     * formatMessageJson - Format text and a message type as JSON for use in returning to the frontend
     *
     * @return - gson Message object
     */
    public Object formatMessageJson(Message.MessageType messageType, String messageText) {
        Message message = new Message(messageText, messageType);
        return gson.toJson(message);
    }
}
