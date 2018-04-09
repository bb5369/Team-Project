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


public class PostResignGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());

    private final GameManager gameManager;

    /**
     * Initializes PostResignGame
     *
     * @param gameManager - used to resign from the game
     */
    public PostResignGameRoute(GameManager gameManager) {
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.gameManager = gameManager;
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
        LOG.finer("PostResignGameRoute is invoked.");

        Player sessionPlayer = request.session().attribute("Player");

        LOG.finer(String.format("Player [%s] wants to resign!", sessionPlayer.getName()));

        boolean resignWorked = gameManager.resignGame(sessionPlayer);

        if (resignWorked) {
            LOG.finer("Resign worked");
			return (new Gson()).toJson(new Message(sessionPlayer.name + "Resigned", Message.MessageType.info));

		} else{
            LOG.finer("Resign failed. Most likely not their turn");
            return (new Gson()).toJson(new Message(sessionPlayer.name + "'s Resign failed", Message.MessageType.error));
        }

    }
}
