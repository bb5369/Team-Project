package com.webcheckers.appl;

import com.webcheckers.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Application-tier")
public class GameManagerTest {

    private static ArrayList<CheckersGame> gameList;

    private GameManager CuT;
    private CheckersGame game;

    private Player redPlayer;
    private Player whitePlayer;
    private Player nullPlayer;
    private Player spectator;


    @BeforeEach
    public void setup(){
        CuT = new GameManager();
        redPlayer = new Player("red", Player.GameType.NORMAL);
        whitePlayer = new Player("white", Player.GameType.NORMAL);
        nullPlayer = null;
        spectator = new Player("spectator", Player.GameType.NORMAL);
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

    @Test
    public void spectator(){
        game = CuT.getNewGame(redPlayer, whitePlayer);
        assertFalse(CuT.isPlayerASpectator(spectator));
        CuT.addSpectator(spectator, redPlayer);
        assertTrue(CuT.isPlayerASpectator(spectator));
        assertEquals(game, CuT.getSpectatorGame(spectator));
        CuT.clearGame(redPlayer);
        assertFalse(CuT.isPlayerASpectator(spectator));
        assertFalse(CuT.isPlayerInAGame(redPlayer));
        assertTrue(CuT.getGameList().isEmpty());
        CuT.getNewGame(redPlayer, whitePlayer);
        CuT.addSpectator(spectator,redPlayer);
        Player spectator2 = new Player("spectator2", Player.GameType.NORMAL);
        CuT.removeSpectator(spectator);
        CuT.addSpectator(spectator2, redPlayer);
        CuT.removeSpectator(spectator2);
        assertFalse(CuT.getGameList().isEmpty());
        assertFalse(CuT.isPlayerASpectator(spectator));
        CuT.clearGames();
        assertFalse(CuT.isPlayerInAGame(redPlayer));
        assertFalse(CuT.isPlayerASpectator(spectator));
        assertNull(CuT.getGame(redPlayer));
    }

    @Test
    public void otherTests(){
        assertFalse(CuT.isPlayerInAGame(redPlayer));
        game = CuT.getNewGame(redPlayer,whitePlayer);
        assertFalse(CuT.isPlayerInAGame(spectator));
        assertEquals(game, CuT.getGame(redPlayer, whitePlayer));
        assertNotEquals(new CheckersGame(redPlayer, spectator), CuT.getGame(redPlayer));
        assertNotEquals(new CheckersGame(spectator, whitePlayer), CuT.getGame(redPlayer));
        assertEquals(redPlayer, CuT.getPlayerTurn(redPlayer).getPlayer());
        assertNull(CuT.getPlayerTurn(whitePlayer));
        Move move = new Move(new Position(5,2), new Position(4,3));
        CuT.getPlayerTurn(redPlayer).validateMove(move);
        assertFalse(CuT.resignGame(redPlayer));
        assertTrue(CuT.resignGame(whitePlayer));
        assertNull(CuT.getNewGame(redPlayer, whitePlayer));
        CuT.clearGames();
        assertEquals(game.toString(), CuT.getGame(redPlayer, whitePlayer).toString());
    }
}
