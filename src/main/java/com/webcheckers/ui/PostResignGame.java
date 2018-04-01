package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;


public class PostResignGame implements Route {
    private static final Logger LOG = Logger.getLogger(PostResignGame.class.getName());

    private final GameManager gameManager;
    private final String info_Message = "Resign the current player from the game";
    private final Gson gson;

    public PostResignGame(TemplateEngine templateEngine, GameManager gameManager, final Gson gson){
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.gameManager = gameManager;
        this.gson = gson;
        LOG.config("PostResignRoute is initialized");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGame is invoked.");

        Player currentPlayer = request.session().attribute("Player");

        gameManager.resignGame(currentPlayer);

        // TODO: If we can't resign from the game then return that message
        return formatMessageJson(Message.MessageType.info, info_Message);
    }
    /**
     * formatMessageJson - Format text and a message type as JSON for use in returning to the frontend
     * @return gson Message object
     */
    public Object formatMessageJson(Message.MessageType messageType, String messageText) {
        Message message = new Message(messageText, messageType);
        return gson.toJson(message);
    }
}
