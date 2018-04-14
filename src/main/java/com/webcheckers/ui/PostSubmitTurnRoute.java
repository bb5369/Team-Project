package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;

import java.util.Objects;
import java.util.logging.Logger;

import spark.Route;
import spark.Request;
import spark.Response;

/**
 * UI Controller for POSTing when a turn is submitted
 */
public class PostSubmitTurnRoute implements Route {

    //need to update the board
    //need game to update board
    //need gameManager to get the game
    //need the player to get the specific game
    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());
    private final GameManager gameManager;

    /**
     * Initializes the PostSubmitTurnRoute
     *
     * @param gameManager - used to get the TurnController
     */
    PostSubmitTurnRoute(GameManager gameManager) {
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.gameManager = gameManager;
        LOG.config("PostSubmitTurnRoute initialized");
    }

    /**
     * Submits completed turns
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - Json message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSubmitTurnRoute invoked");

        Player sessionPlayer = request.session().attribute("Player");
        CheckersGame game = gameManager.getGame(sessionPlayer);

        if(game.submitTurn(sessionPlayer)) {
            return (new Gson()).toJson(new Message("Move Made", Message.MessageType.info));

        } else {
            if(!game.getTurn().mulitJumpDone())
                return (new Gson()).toJson(new Message("You have to continue the multi-jump", Message.MessageType.error));
            return (new Gson()).toJson(new Message("No move has been made", Message.MessageType.error));
        }
    }
}
