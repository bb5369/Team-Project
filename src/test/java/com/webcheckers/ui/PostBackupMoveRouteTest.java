package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Player;
import com.webcheckers.model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostBackupMoveRouteTest {

    private PostBackupMoveRoute CuT;

    private Request request;
    private Response response;
    private Session session;

    private GameManager gameManager;
    private Player currPlayer;
    private Turn turn;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

        gameManager = mock(GameManager.class);
        turn = mock(Turn.class);

        CuT = new PostBackupMoveRoute(gameManager);
    }

    @Test
    public void runTrue(){
        currPlayer = new Player("redPlayer");
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getPlayerTurn(currPlayer)).thenReturn(turn);
        when(turn.backupMove()).thenReturn(true);

        CuT.handle(request, response);
    }

    @Test
    public void runFalse(){
        currPlayer = new Player("redPlayer");
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getPlayerTurn(currPlayer)).thenReturn(turn);
        when(turn.backupMove()).thenReturn(false);

        CuT.handle(request, response);
    }

}
