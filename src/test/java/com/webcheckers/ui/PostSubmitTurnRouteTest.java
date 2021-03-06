package com.webcheckers.ui;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Message;
import com.webcheckers.model.Player;
import com.webcheckers.model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

    private CheckersGame game;
    private GameManager gameManager;
    private Player player;

    @BeforeEach
    public void setup(){
        player = new Player("redPlayer", Player.GameType.NORMAL);

        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        game = mock(CheckersGame.class);
        gameManager = mock(GameManager.class);

        when(session.attribute("Player")).thenReturn(player);
        when(gameManager.getGame(player)).thenReturn(game);

        CuT = new PostSubmitTurnRoute(gameManager);
    }

    @Test
    public void runCorrect(){
        when(session.attribute("Player")).thenReturn(player);
        when(gameManager.getGame(player)).thenReturn(game);
        when(game.submitTurn(player)).thenReturn(new Message("message", Message.MessageType.info));

        CuT.handle(request, response);
    }

}
