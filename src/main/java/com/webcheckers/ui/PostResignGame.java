package com.webcheckers.ui;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;


public class PostResignGame implements Route {
    private static final Logger LOG = Logger.getLogger(PostResignGame.class.getName());

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;
    private final String info_Message = "Resign the current player from the game";
    private final String error_Message = "This player cannot currently resign";

    public PostResignGame(TemplateEngine templateEngine, PlayerLobby playerLobby, GameManager gameManager){
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "player must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.gameManager = gameManager;

        LOG.config("PostResignRoute is initialized");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGame is invoked.");

        Player currentPlayer = request.session().attribute("Player");
        Message message;
        message = new Message(info_Message, Message.MessageType.info);
        response.body(message.getText());
        gameManager.resignGame(currentPlayer);
        response.redirect(WebServer.HOME_URL);
        return null;
    }
}
