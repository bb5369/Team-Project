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
public class PostEndSpectateRouteTest {
    // The component-under-test (CuT).
    private PostEndSpectateRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private GameManager gameManager;

    private Player red = new Player("redPlayer", Player.GameType.NORMAL);
    private Player spectator = new Player ("spectator", Player.GameType.NORMAL);

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

        this.gameManager = new GameManager();

        gameManager.getNewGame(red, new Player("white", Player.GameType.NORMAL));
        gameManager.addSpectator(spectator, red);

        CuT = new PostEndSpectateRoute(gameManager);
    }

    @Test
    public void handle(){
        when(request.session().attribute("Player")).thenReturn(spectator);

        CuT.handle(request, response);

        verify(response,times(1)).redirect(WebServer.HOME_URL);
        assertFalse(gameManager.isPlayerASpectator(spectator));
    }
}
