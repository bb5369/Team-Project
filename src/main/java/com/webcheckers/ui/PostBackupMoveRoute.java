package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Turn;
import com.webcheckers.model.Message;
import com.google.gson.Gson;

import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * UI controller for POSTing a move backup
 */
public class PostBackupMoveRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());

    private final GameManager gameManager;

    /**
     * Initializes the PostBackupMoveRoute
     *
     * @param gameManager - used to get the turn controller
     */
    PostBackupMoveRoute(GameManager gameManager) {
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        this.gameManager = gameManager;

        LOG.config("PostBackupMoveRoute initialized");
    }

    /**
     * Backs up the move
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - Json message
     * @throws Exception
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostBackupMoveRoute invoked");
        Player sessionPlayer = request.session().attribute("Player");

        Turn turn = gameManager.getPlayerTurn(sessionPlayer);

        if(turn.backupMove()){
            return (new Gson()).toJson(new Message("Backed a move", Message.MessageType.info));
        } else {
            return (new Gson()).toJson(new Message("Backup failed", Message.MessageType.error));
        }
    }
}
