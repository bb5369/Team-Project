package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Move;
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
public class PostValidateMoveRouteTest {

    private PostValidateMoveRoute CuT;

    private Request request;
    private Response response;
    private Session session;

    private GameManager gameManager;
    private Gson gson;
    private Player currPlayer;
    private Turn turn;
    private Move move;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

        gameManager = mock(GameManager.class);
        gson = new Gson();
        turn = mock(Turn.class);
        move = mock(Move.class);

        CuT = new PostValidateMoveRoute(gson, gameManager);
    }

    @Test
    public void isNotEmptyValid(){
        currPlayer = new Player("redPlayer");
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getPlayerTurn(currPlayer)).thenReturn(turn);
        when(request.body()).thenReturn("{\"start\":{\"row\":5,\"cell\":2},\"end\":{\"row\":4,\"cell\":3}}");
        //when(turn.validateMove(move)).thenReturn(true);

        CuT.handle(request, response);
    }

    @Test
    public void isEmptyNotValid(){
        currPlayer = new Player("redPlayer");
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getPlayerTurn(currPlayer)).thenReturn(turn);
        when(request.body()).thenReturn("");
        //when(turn.validateMove(move)).thenReturn(false);

        CuT.handle(request, response);
    }

}
