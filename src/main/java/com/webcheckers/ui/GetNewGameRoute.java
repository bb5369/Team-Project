package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetNewGameRoute implements Route {

	private static String PLAYER_IN_GAME_MESSAGE = "The player you've selected is already in a game.";
	private static String PLAYER_NOT_EXIST_MESSAGE = "The player by that name does not exist";
    static final String VIEW_NAME = "game.ftl";

	private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameManager gameManager;

    GetNewGameRoute(TemplateEngine templateEngine, final PlayerLobby playerLobby, final GameManager gameManager){
        Objects.requireNonNull(playerLobby, "templateEngine must not be null");
        Objects.requireNonNull(gameManager, "gameManager must not be null");

        this.playerLobby = playerLobby;
        this.gameManager = gameManager;
        this.templateEngine = templateEngine;
    }

    /**
	 * Looks for an ?opponent= query string
     * attempts to setup a new CheckersGame with the opponent and the current session player
     * @param request
     * @param response
     * @return redirection to home or game
     */
    @Override
    public Object handle(Request request, Response response) {
    	// TODO: refactor this whole damn thing

        final String redPlayerName = request.queryParams("redPlayer");
        final String whitePlayerName = request.queryParams("whitePlayer");
        //System.out.println("redPlayer: " + redPlayerName + " whitePlayer: " + whitePlayerName);

        // make sure we have an ?opponent (This should never happen
        if (redPlayerName == null) {
            // TODO: indicate to user why this happened
            response.redirect(WebServer.HOME_URL);
        }

        // get opponent player model
		final Player redPlayer = playerLobby.getPlayer(redPlayerName);
        final Player whitePlayer = playerLobby.getPlayer(whitePlayerName);

        if (redPlayer == null)//this should never happen
        {
			request.session().attribute("message", new Message(PLAYER_NOT_EXIST_MESSAGE, Message.MessageType.error));
			response.redirect(WebServer.HOME_URL);
		}

//        // get my player model
//		final Player sessionPlayer = request.session().attribute("Player");

        // check to see if either of us are in a game
		if (gameManager.isPlayerInGame(redPlayer) || gameManager.isPlayerInGame(whitePlayer))
		{
			request.session().attribute("message", new Message(PLAYER_IN_GAME_MESSAGE, Message.MessageType.error));
			response.redirect(WebServer.HOME_URL);
		}
		else
        {
            gameManager.getNewGame(redPlayer, whitePlayer);
            response.redirect(WebServer.GAME_URL);

			return null;
        }

        // get a new game with our models

		// Direct player at /game

        return null;
    }
}
