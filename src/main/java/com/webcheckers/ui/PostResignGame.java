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

    public PostResignGame(GameManager gameManager) {
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.gameManager = gameManager;
        LOG.config("PostResignRoute is initialized");
    }

    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGame is invoked.");

        Player sessionPlayer = request.session().attribute("Player");

        boolean resignWorked = gameManager.resignGame(sessionPlayer);

        if (resignWorked)
            return (new Gson()).toJson(new Message(sessionPlayer.name + "Resigned", Message.MessageType.info));

        else
            return (new Gson()).toJson(new Message(sessionPlayer.name + "'s Resign failed", Message.MessageType.error));

    }
}
