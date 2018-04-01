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

public class PostBackupMoveRoute implements Route{
//    private final GameManager gameManager;
//    private final TemplateEngine templateEngine;
    private final GameManager gameManager;
    private final PlayerLobby playerLobby;

    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());

    PostBackupMoveRoute(PlayerLobby playerLobby, GameManager gameManager) {
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");
        this.gameManager = gameManager;
        this.playerLobby = playerLobby;

        LOG.config("PostBackupMoveRoute initialized");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostBackupMoveRoute invoked");
        Player sessionPlayer = request.session().attribute("Player");
        Turn turn = gameManager.getTurnController(sessionPlayer);

        if(turn.backupMove()){
            return (new Gson()).toJson(new Message("Backed a move", Message.MessageType.info));
        }
        else{
            return (new Gson()).toJson(new Message("Backup failed", Message.MessageType.error));
        }
    }
}
