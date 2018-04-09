package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class GetHomeRouteTest {

    // Component under test
    private GetHomeRoute CuT;

    private Request request;
    private Response response;
    private Session session;

    private PlayerLobby playerLobby;
    private GameManager gameManager;
    private TemplateEngine templateEngine;

    private Player currPlayer;
    private Message message;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);
        templateEngine = mock(TemplateEngine.class);

        CuT = new GetHomeRoute(templateEngine, playerLobby, gameManager);
    }

    @Test
    public void playerInLobbyNotNull(){
        currPlayer = new Player("redPlayer");
        Message.MessageType type = Message.MessageType.info;
        CheckersGame game = mock(CheckersGame.class);
        message = new Message("message text", type);

        when(session.attribute("Player")).thenReturn(currPlayer);
        when(playerLobby.isPlayerInLobby(currPlayer)).thenReturn(true);
        when(gameManager.getGame(currPlayer)).thenReturn(game);
        when(game.isResigned()).thenReturn(false);
        when(gameManager.isPlayerInAGame(currPlayer)).thenReturn(true);
        when(request.session().attribute("message")).thenReturn(message);

        CuT.handle(request, response);

        verify(response, times(1)).redirect(WebServer.GAME_URL);
    }

    @Test
    public void playerNotInLobbyNotNull(){
        currPlayer = new Player("redPlayer");
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(playerLobby.isPlayerInLobby(currPlayer)).thenReturn(true);
        when(gameManager.isPlayerInAGame(currPlayer)).thenReturn(false);
        when(request.session().attribute("message")).thenReturn(null);

        CuT.handle(request, response);
    }

    @Test
    public void playerInLobbyNull(){
        currPlayer = null;
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(playerLobby.isPlayerInLobby(currPlayer)).thenReturn(true);
        when(gameManager.isPlayerInAGame(currPlayer)).thenReturn(false);

        CuT.handle(request, response);
    }

    @Test
    public void playerNotInLobbyNull(){
        currPlayer = null;
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(playerLobby.isPlayerInLobby(currPlayer)).thenReturn(false);
        when(gameManager.isPlayerInAGame(currPlayer)).thenReturn(false);

        CuT.handle(request, response);
    }

}
