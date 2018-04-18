package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UI-tier")
public class GetSpectateRouteTest {
    // The component-under-test (CuT).
    private GetSpectateRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerLobby;
    private GameManager gameManager;

    private Player red = new Player("redPlayer");
    private Player spectator = new Player ("spectator");

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

        this.playerLobby = new PlayerLobby();
        this.gameManager = new GameManager();

        playerLobby.newPlayer(red.getName());
        playerLobby.newPlayer("white");
        gameManager.getNewGame(red, new Player("white"));

        CuT = new GetSpectateRoute(playerLobby, gameManager);
    }

    @Test
    public void handle(){
        when(request.queryParams("redPlayer")).thenReturn(red.getName());
        when(request.session().attribute("Player")).thenReturn(spectator);

        CuT.handle(request, response);

        verify(response,times(1)).redirect(WebServer.GAME_URL);
        assertTrue(gameManager.isPlayerASpectator(spectator));
    }
}
