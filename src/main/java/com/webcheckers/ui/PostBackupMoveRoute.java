package com.webcheckers.ui;

import com.webcheckers.model.Message;
import com.webcheckers.model.Move;
import com.google.gson.Gson;

import spark.*;

import java.util.PriorityQueue;
import java.util.logging.Logger;

public class PostBackupMoveRoute implements Route{
//    private final GameManager gameManager;
//    private final TemplateEngine templateEngine;

    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    PostBackupMoveRoute() {

//        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
//        this.templateEngine = templateEngine;

        LOG.config("PostBackupMoveRoute initialized");
    }


    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostBackupMoveRoute invoked");

        PriorityQueue<Move> emptyMoves= request.session().attribute("turnMoveList");
        boolean backupWorked = emptyMoves.removeAll(emptyMoves);
        request.session().attribute("turnMoveList", emptyMoves);

        if(backupWorked) {
            return (new Gson()).toJson(new Message("Backed a move", Message.MessageType.info));
        }
        else{
            return (new Gson()).toJson(new Message("Backup failed", Message.MessageType.error));
        }
    }
}
