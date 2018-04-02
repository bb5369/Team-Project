package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class GetClearRouteTest {

    // Component under test
    private GetClearRoute CuT;

    private Request request;
    private Response response;
    private Session session;

    private PlayerLobby playerLobby;
    private GameManager gameManager;

    private Player currPlayer;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);

        playerLobby = mock(PlayerLobby.class);
        gameManager = mock(GameManager.class);

        CuT = new GetClearRoute(playerLobby, gameManager);
    }

    @Test
    public void runAdmin(){
        currPlayer = new Player("adminDude");
        when(session.attribute("Player")).thenReturn(currPlayer);

        CuT.handle(request, response);
    }

    @Test
    public void runNull(){
        currPlayer = null;
        when(session.attribute("Player")).thenReturn(currPlayer);

        CuT.handle(request, response);
    }

    @Test
    public void runNormal(){
        currPlayer = new Player("bro");
        when(session.attribute("Player")).thenReturn(currPlayer);

        CuT.handle(request, response);
    }

}
