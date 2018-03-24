package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.MoveValidator;
import com.webcheckers.model.*;

import com.google.gson.Gson;

import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.model.Message.MessageType.error;

public class PostValidateMoveRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    private final String NO_POSITION_PROVIDED_MESSAGE = "No position was provided for validation.";
    private final String NOT_YOUR_TURN_MESSAGE = "You cannot validate a move if it is not your turn";

    private final Gson gson;
    private final GameManager gameManager;
    private final MoveValidator moveValidator;

    PostValidateMoveRoute(final Gson gson,
                          final GameManager gameManager,
                          final MoveValidator moveValidator) {

        Objects.requireNonNull(gameManager, "gameManager must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(moveValidator, "moveValidator must not be null");

        this.gson = gson;
        this.gameManager = gameManager;
        this.moveValidator = moveValidator;

        LOG.config("PostValidateMoveRoute initialized");
    }

    /**
     * Accepts a player name input from the Sign-in form and attempts to create a new player
     * in the lobby. Passes on errors to the client UI, or moves the user to a signed-in homepage.
     * @param request
     * @param response
     * @return FreeMarker rendered template
     */
    @Override
    public Object handle(Request request, Response response) {
    	LOG.finer("PostValidateMoveRoute invoked");

        String positionAsJson = request.body();
        Player sessionPlayer = request.session().attribute("Player");

        CheckersGame activeGame = gameManager.getGame(sessionPlayer);

        if ( ! activeGame.getPlayerActive().equals(sessionPlayer)) {
            return formatMessageJson(Message.MessageType.error, NOT_YOUR_TURN_MESSAGE);
        }

        if (positionAsJson.isEmpty()) {
            return formatMessageJson(Message.MessageType.error, NO_POSITION_PROVIDED_MESSAGE);
        }

        Move requestedMove = gson.fromJson(positionAsJson, Move.class);

        // "RED Player [username] wants to move from <0,1> to <1,2>"
        LOG.finest(String.format("%s Player [%s] wants to %s",
                activeGame.getPlayerColor(sessionPlayer),
                sessionPlayer.getName(),
                formatMoveLogMessage(requestedMove)));

        boolean isValidMove = moveValidator.validateMove(activeGame, sessionPlayer, requestedMove);

        if (isValidMove) {
            return formatMessageJson(Message.MessageType.info, "Good move");
        } else {
            // TODO: it says we should explain why the move was not valid
            // IE destination space occupied, space is invalid
            return formatMessageJson(Message.MessageType.error, "Your move was not valid EXPLAIN");
        }
    }

    /**
     * formatMessageJson - Format text and a message type as JSON for use in returning to the frontend
     * @param messageType
     * @param messageText
     * @return
     */
    public Object formatMessageJson(Message.MessageType messageType, String messageText) {
        Message message = new Message(messageText, messageType);

        return gson.toJson(message);
    }

    /**
     * Helper function used for object level debug logging of a move
     * @param move
     * @return
     */
    private String formatMoveLogMessage(Move move) {
        int startX, startY, endX, endY;

        startX = move.getStart().getCell();
        startY = move.getStart().getRow();

        endX = move.getEnd().getCell();
        endY = move.getEnd().getRow();

        return String.format("move from <%d,%d> to <%d,%d>",
                startX, startY,
                endX, endY);

    }
}
