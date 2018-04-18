package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameManager;
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
public class PostCheckTurnRouteTest {

    private PostCheckTurnRoute CuT;

    private Request request;
    private Response response;
    private Session session;
    private TemplateEngine templateEngine;

    private GameManager gameManager;
    private CheckersGame game;
    private Gson gson;
    private Player currPlayer;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);

        gameManager = mock(GameManager.class);
        gson =  new Gson();

        CuT = new PostCheckTurnRoute(gameManager, gson);
    }

    @Test
    public void gameNotNullNorResignedIsWon(){
        currPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        game = mock(CheckersGame.class);
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getGame(currPlayer)).thenReturn(game);
        when(game.isResigned()).thenReturn(false);
        when(game.isWon()).thenReturn(true);
        when(game.getWinner()).thenReturn(currPlayer);

        CuT.handle(request, response);
    }

    @Test
    public void gameNotNullNorResignedNotWonIsEqual(){
        currPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        game = mock(CheckersGame.class);
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getGame(currPlayer)).thenReturn(game);
        when(game.isResigned()).thenReturn(false);
        when(game.isWon()).thenReturn(false);
        when(game.getPlayerActive()).thenReturn(currPlayer);

        CuT.handle(request, response);
    }

    @Test
    public void gameNotNullNorResignedNotWonNotEqual(){
        currPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        game = mock(CheckersGame.class);
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getGame(currPlayer)).thenReturn(game);
        when(game.isResigned()).thenReturn(false);
        when(game.isWon()).thenReturn(false);
        when(game.getPlayerActive()).thenReturn(null);

        CuT.handle(request, response);
    }

    @Test
    public void gameNotNullAndResigned(){
        currPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        game = mock(CheckersGame.class);
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getGame(currPlayer)).thenReturn(game);
        when(game.isResigned()).thenReturn(true);
        when(game.getLoser()).thenReturn(currPlayer);
        when(game.getWinner()).thenReturn(currPlayer);
        
        CuT.handle(request, response);
    }

    @Test
    public void gameNull(){
        currPlayer = new Player("redPlayer", Player.GameType.NORMAL);
        game = null;
        when(session.attribute("Player")).thenReturn(currPlayer);
        when(gameManager.getGame(currPlayer)).thenReturn(game);

        CuT.handle(request, response);
    }

}
