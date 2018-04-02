package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import spark.*;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class GetSignOutRouteTest {

    /**
     * Component under test
     */
    private GetSignOutRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;
    private TemplateEngineTester testHelper;
    private PlayerLobby playerLobby;
    private GameManager gameManager;

    private Player playerRed;
    private Player playerWhite;
    private CheckersGame game;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        playerRed = mock(Player.class);
        playerWhite = new Player("white");
        templateEngine = mock(TemplateEngine.class);
        testHelper = new TemplateEngineTester();
        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);

        CuT = new GetSignOutRoute(playerLobby, gameManager);
    }

    @Test
    public void notInPlayerLobbyGameNotNull() {
        when(playerRed.getName()).thenReturn("red");
        game = new CheckersGame(playerRed, playerWhite);
        when(session.attribute("Player")).thenReturn(playerRed);
        when(gameManager.getGame(playerRed)).thenReturn(game);
        when(playerLobby.isPlayerInLobby(playerRed)).thenReturn(false);

        CuT.handle(request, response);

        verify(response, times(1)).redirect(WebServer.HOME_URL);
    }
;

    @Test
    public void inPlayerLobbyGameNotNull(){
        when(playerRed.getName()).thenReturn("red");
        game = new CheckersGame(playerRed, playerWhite);
        when(session.attribute("Player")).thenReturn(playerRed);
        when(gameManager.getGame(playerRed)).thenReturn(game);
        when(playerLobby.isPlayerInLobby(playerRed)).thenReturn(true);

        CuT.handle(request, response);

        verify(response, times(1)).redirect(WebServer.HOME_URL);
    }

    @Test
    public void notInPlayerLobbyGameNull() {
        when(playerRed.getName()).thenReturn("red");
        game = null;
        when(session.attribute("Player")).thenReturn(playerRed);
        when(gameManager.getGame(playerRed)).thenReturn(game);
        when(playerLobby.isPlayerInLobby(playerRed)).thenReturn(false);

        CuT.handle(request, response);

        verify(response, times(1)).redirect(WebServer.HOME_URL);
    }

    @Test
    public void inPlayerLobbyGameNull(){
        when(playerRed.getName()).thenReturn("red");
        game = null;
        when(session.attribute("Player")).thenReturn(playerRed);
        when(gameManager.getGame(playerRed)).thenReturn(game);
        when(playerLobby.isPlayerInLobby(playerRed)).thenReturn(true);

        CuT.handle(request, response);

        verify(response, times(1)).redirect(WebServer.HOME_URL);
    }

}
