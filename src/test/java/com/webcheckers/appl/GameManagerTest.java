package com.webcheckers.appl;

import com.webcheckers.model.CheckersGame;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@Tag("Application-tier")
public class GameManagerTest {

    private static ArrayList<CheckersGame> gameList;

    private GameManager CuT;
    private CheckersGame game;

    private Player redPlayer;
    private Player whitePlayer;
    private Player nullPlayer;


    @BeforeEach
    public void setup(){
        CuT = new GameManager();
        redPlayer = mock(Player.class);
        whitePlayer = mock(Player.class);
        nullPlayer = null;
    }

    @Test
    public void testConstructor(){
        new GameManager();
    }

    @Test
    public void testIsInGameFalse(){
        boolean inGame = CuT.isPlayerInAGame(redPlayer);
        assertEquals(false, inGame);
        inGame = CuT.isPlayerInAGame(whitePlayer);
        assertEquals(false, inGame);
    }

    @Test
    public void testIsInGameTrue(){
        game = CuT.getNewGame(redPlayer, whitePlayer);
        boolean inGame = CuT.isPlayerInAGame(redPlayer);
        assertEquals(true, inGame);
        inGame = CuT.isPlayerInAGame(whitePlayer);
        assertEquals(true, inGame);
    }

    @Test
    public void testIsInGameNull(){
        boolean inGame = CuT.isPlayerInAGame(nullPlayer);
        assertEquals(false, inGame);
    }

}
