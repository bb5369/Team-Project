package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import com.webcheckers.model.TournamentScoreboard;
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
    private TournamentScoreboard tournamentScoreboard;

    private Player player1;
    private Player player2;
    @BeforeEach
    public void setup(){
        player1 = new Player("PlayerOne", Player.GameType.NORMAL);

        request = mock(Request.class);
        session = mock(Session.class);
        response = mock(Response.class);
        gameManager = mock(GameManager.class);
        tournamentScoreboard = mock(TournamentScoreboard.class);

        when(request.session()).thenReturn(session);
        when(session.attribute("Player")).thenReturn(player1);

        CuT = new PostResignGameRoute(gameManager, tournamentScoreboard);
    }

    @Test
    public void resign_success() {
        when(gameManager.resignGame(player1)).thenReturn(true);

        Object responseString = CuT.handle(request, response);
        Object gson = new Gson().toJson(new Message(player1.name + "Resigned", Message.MessageType.info));

        assertEquals(responseString, gson);
    }

    @Test
    public void resign_fail() {
        when(gameManager.resignGame(player1)).thenReturn(false);


        Object responseString = CuT.handle(request, response);
        Object gson = new Gson().toJson(new Message(player1.name + "'s Resign failed", Message.MessageType.error));

        assertEquals(responseString, gson);
    }

}
