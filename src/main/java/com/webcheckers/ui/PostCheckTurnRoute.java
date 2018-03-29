package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

public class PostCheckTurnRoute implements Route {

    private final GameManager gameManager;
    private final PlayerLobby playerLobby;
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());


    public PostCheckTurnRoute(PlayerLobby playerLobby, GameManager gameManager)
    {
        this.gameManager = gameManager;
        this.playerLobby = playerLobby;

        LOG.config("PostCheckTurnRoute initialized");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return (new Gson()).toJson(new Message("true", Message.MessageType.info));
    }
}
