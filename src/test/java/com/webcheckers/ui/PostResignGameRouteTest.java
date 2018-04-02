package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import com.google.gson.Gson;

import static org.mockito.Mockito.*;

@Tag("UI-tier")
public class PostResignGameRouteTest {
    // The component-under-test (CuT).
    private PostResignGameRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private GameManager gameManager;

    private Player player1;
    private Player player2;
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);

        player1 = new Player("PlayerOne");
        player2 = new Player("PlayerTwo");
        gameManager = new GameManager();

        gameManager.getNewGame(player1, player2);
        CuT = new PostResignGameRoute(gameManager);
    }

    @Test
    public void test() {
        when(session.attribute("Player")).thenReturn(player1);

        Object responseString = CuT.handle(request, response);
        Object gson = new Gson().toJson(new Message(player1.name + "Resigned", Message.MessageType.info));

        assertEquals(responseString, gson);
    }

    @Test
    public void test2(){
        gameManager.clearGames();
        when(session.attribute("Player")).thenReturn(player2);

        Object responseString = CuT.handle(request, response);
        Object gson = new Gson().toJson(new Message(player2.name + "'s Resign failed", Message.MessageType.error));

        assertEquals(responseString, gson);
    }
}
