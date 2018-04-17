package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Turn;
import com.webcheckers.model.*;

import com.google.gson.Gson;

import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * UI Controller to POST when move is validated
 */
public class PostValidateMoveRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    private final String NO_POSITION_PROVIDED_MESSAGE = "No position was provided for validation.";
    private final String INVALID_MOVE_MESSAGE = "The move you requested is not valid!";

    private final Gson gson;
    private final GameManager gameManager;

    /**
     * Initializes the PostValidateMoveRoute
     *
     * @param gson        - used to pass a message to AJAX
     * @param gameManager - used to access TurnController
     */
    PostValidateMoveRoute(final Gson gson,
                          final GameManager gameManager) {

        Objects.requireNonNull(gameManager, "gameManager must not be null");
        Objects.requireNonNull(gson, "gson must not be null");

        this.gson = gson;
        this.gameManager = gameManager;

        LOG.config("PostValidateMoveRoute initialized");
    }

    /**
     * Accepts a player name input from the Sign-in form and attempts to create a new player
     * in the lobby. Passes on errors to the client UI, or moves the user to a signed-in homepage.
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - FreeMarker rendered template
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostValidateMoveRoute invoked");

        try {
            if(request.body().contains("null")){
                throw new Error("The move is invalid");
            }
            Player sessionPlayer = request.session().attribute("Player");
            Turn turn = gameManager.getPlayerTurn(sessionPlayer);
            String positionAsJson = request.body();
            LOG.finest(String.format("JSON body: [%s]", positionAsJson));

            if (positionAsJson.isEmpty()) {
                return formatMessageJson(Message.MessageType.error, NO_POSITION_PROVIDED_MESSAGE);
            }

            Move requestedMove = gson.fromJson(positionAsJson, Move.class);

            return turn.validateMove(requestedMove).toJson();

        } catch(Error e){
            LOG.warning(e.getMessage());
            return formatMessageJson(Message.MessageType.error, "You can't move to a space occupied by a piece");
        }
    }

    /**
     * formatMessageJson - Format text and a message type as JSON for use in returning to the frontend
     *
     * @param messageType - type of message (info/error)
     * @param messageText - contents of the message
     * @return - gson Message object
     */
    private Object formatMessageJson(Message.MessageType messageType, String messageText) {
        Message message = new Message(messageText, messageType);

        return gson.toJson(message);
    }
}
