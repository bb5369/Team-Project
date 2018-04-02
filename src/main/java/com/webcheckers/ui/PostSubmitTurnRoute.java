package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Turn;
import com.webcheckers.model.Message;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Player;
import spark.Route;
import spark.Request;
import spark.Response;

public class PostSubmitTurnRoute implements Route {

    //need to update the board
    //need game to update board
    //need gameManager to get the game
    //need the player to get the specific game
    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;

    PostSubmitTurnRoute(PlayerLobby playerLobby, GameManager gameManager) {
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.gameManager = gameManager;
        this.playerLobby = playerLobby;
        LOG.config("PostSubmitTurnRoute initialized");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostSubmitTurnRoute invoked");

        Player sessionPlayer = request.session(). attribute("Player");
        Turn turn = gameManager.getPlayerTurn(sessionPlayer);

        if(turn.submitTurn()) { //making sure a move was actually made
            return (new Gson()).toJson(new Message("Move Made", Message.MessageType.info));
            //update the board model here making it permanent
        }
        else {// error because no move was made
            return (new Gson()).toJson(new Message("No move has been made", Message.MessageType.error));
        }
    }
}
