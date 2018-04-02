package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import com.webcheckers.model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class PostSubmitTurnRouteTest {

    private PostSubmitTurnRoute CuT;

    private Request request;
    private Response response;
    private Session session;
    private TemplateEngine templateEngine;

    private GameManager gameManager;
    private Player currPlayer;
    private Turn turn;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);

        gameManager = mock(GameManager.class);
        turn = mock(Turn.class);

        CuT = new PostSubmitTurnRoute(gameManager);
    }

    @Test
    public void runCorrect(){
        currPlayer = new Player("redPlayer");
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getPlayerTurn(currPlayer)).thenReturn(turn);
        when(turn.submitTurn()).thenReturn(true);

        CuT.handle(request, response);
    }

    @Test
    public void runFail(){
        currPlayer = new Player("redPlayer");
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getPlayerTurn(currPlayer)).thenReturn(turn);
        when(turn.submitTurn()).thenReturn(false);

        CuT.handle(request, response);
    }

}
