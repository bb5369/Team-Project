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
    private final String thisPlayersTurn = "true";
    private final String otherPlayersTurn = "false";

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
        //System.out.println("PostCheckTurn");
        Player currentPlayer = request.session().attribute("Player");
        CheckersGame game = gameManager.getGame(currentPlayer);
        if(game.isResignedPlayer(game.getOtherPlayer(currentPlayer))){
            //gameManager.clearResigned(currentPlayer);
            return formatMessageJson(opponentResigned);
        }
        else if(game.getTurn().getPlayer().equals(currentPlayer)){
            return formatMessageJson(thisPlayersTurn);
        }
        else {
            return formatMessageJson(otherPlayersTurn);
        }
    }

    /**
     * formatMessageJson - Format text and a message type as JSON for use in returning to the frontend
     *
     * @return - gson Message object
     */
    public Object formatMessageJson(String message) {
        return gson.toJson(new Message(message, Message.MessageType.info));
    }
}

