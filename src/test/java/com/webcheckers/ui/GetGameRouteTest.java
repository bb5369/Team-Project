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
public class GetGameRouteTest {

    // Component under test
    private GetGameRoute CuT;

    private Request request;
    private Response response;
    private Session session;

    private PlayerLobby playerLobby;
    private GameManager gameManager;
    private TemplateEngine templateEngine;
    private CheckersGame game;

    private Player redPlayer;
    private Player whitePlayer;
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

        CuT = new GetGameRoute(templateEngine, playerLobby, gameManager);
    }

    @Test
    public void inGameRed(){
        redPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        whitePlayer = new Player("whitePlayer", Player.GameType.NORMAL);
        game = new CheckersGame(redPlayer, whitePlayer);
        when(session.attribute("Player")).thenReturn(redPlayer);
        when(playerLobby.getPlayer("whitePlayer")).thenReturn(whitePlayer);
        when(gameManager.isPlayerInAGame(redPlayer)).thenReturn(true);
        when(gameManager.getGame(redPlayer)).thenReturn(game);
        CuT.handle(request, response);
    }

    @Test
    public void inGameWhite(){
        redPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        whitePlayer = new Player("whitePlayer", Player.GameType.NORMAL);
        game = new CheckersGame(redPlayer, whitePlayer);
        when(session.attribute("Player")).thenReturn(whitePlayer);
        when(playerLobby.getPlayer("whitePlayer")).thenReturn(whitePlayer);
        when(gameManager.isPlayerInAGame(whitePlayer)).thenReturn(true);
        when(gameManager.getGame(whitePlayer)).thenReturn(game);
        CuT.handle(request, response);
    }

    @Test
    public void notInGameRed(){
        redPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        whitePlayer = new Player("whitePlayer", Player.GameType.NORMAL);
        game = new CheckersGame(redPlayer, whitePlayer);
        when(session.attribute("Player")).thenReturn(redPlayer);
        //when(playerLobby.getPlayer("whitePlayer")).thenReturn(whitePlayer);
        when(gameManager.isPlayerInAGame(redPlayer)).thenReturn(false);
        when(playerLobby.getPlayer(request.queryParams("whitePlayer"))).thenReturn(whitePlayer);
        when(gameManager.getGame(redPlayer)).thenReturn(game);

        CuT.handle(request, response);
    }

    @Test
    public void notInGameWhite(){
        redPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        whitePlayer = new Player("whitePlayer", Player.GameType.NORMAL);
        game = new CheckersGame(redPlayer, whitePlayer);
        when(session.attribute("Player")).thenReturn(whitePlayer);
        when(playerLobby.getPlayer("whitePlayer")).thenReturn(whitePlayer);
        when(gameManager.isPlayerInAGame(whitePlayer)).thenReturn(false);
        when(playerLobby.getPlayer(request.queryParams("whitePlayer"))).thenReturn(whitePlayer);
        when(gameManager.getGame(whitePlayer)).thenReturn(game);

        CuT.handle(request, response);
    }

    @Test
    public void nullGuy(){
        redPlayer = null;
        CuT.handle(request, response);

        verify(response, times(1)).redirect(WebServer.HOME_URL);
    }

}
