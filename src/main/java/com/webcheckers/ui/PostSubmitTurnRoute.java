package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Message;
import com.webcheckers.model.Move;

import java.util.PriorityQueue;
import java.util.logging.Logger;

import spark.Route;
import spark.Request;
import spark.Response;

public class PostSubmitTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());
    //need to update the board
    //need game to update board
    //need gameManager to get the game
    //need the player to get the specific game

    PostSubmitTurnRoute() {


        LOG.config("PostSubmitTurnRoute initialized");
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("PostSubmitTurnRoute invoked");

        PriorityQueue<Move> movesMadeList = request.session().attribute("movesMadeList");
        PriorityQueue<Move> turnMovesList = request.session().attribute("turnMoveList");

        if(movesMadeList == null) //if its the first move
        {
            if(turnMovesList != null) { //making sure a move was actually made
                request.session().attribute("movesMade", turnMovesList);
                //update the board model here making it permanent
            }
            else {// error because no move was made
                return (new Gson()).toJson(new Message("No move has been made", Message.MessageType.error));
            }
        }

        //its not the first move
        //go through every move in the turnMovesList
        // and update the model
        // also update the movesMadeList

        //submit updated moves made to the sessions
        request.session().attribute("movesMade", movesMadeList);

        return (new Gson()).toJson(new Message("submit move failed", Message.MessageType.error));
    }
}
