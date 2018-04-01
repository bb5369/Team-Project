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

public class PostCheckTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private final TemplateEngine templateEngine;

    private final GameManager gameManager;
    private final PlayerLobby playerLobby;

    private final String opponentResigned = "true"/*"Your opponent has resigned From the game"*/;

    private final Gson gson;

    public PostCheckTurnRoute(TemplateEngine templateEngine, PlayerLobby playerLobby, GameManager gameManager, Gson gson) {
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "player must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        Objects.requireNonNull(gson, "gson must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
        this.gson = gson;

        LOG.config("PostCheckTurnRoute is initialized");
    }

    @Override
    public Object handle(Request request, Response response){
        LOG.finer("PostCheckTurnRoute is invoked.");
        //System.out.println("PostCheckTurn");
        Player currrentPlayer = request.session().attribute("Player");
        CheckersGame game = gameManager.getGame(currrentPlayer);
        if(game != null){
            Message message = new Message(opponentResigned, Message.MessageType.info);
            gameManager.clearGame(currrentPlayer);
            //gameManager.clearResigned(currrentPlayer);
            return formatMessageJson(message);
        }
        return null;
    }

    /**
     * formatMessageJson - Format text and a message type as JSON for use in returning to the frontend
     * @return gson Message object
     */
    public Object formatMessageJson(Message message) {
        return gson.toJson(message);
    }
}

