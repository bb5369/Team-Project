package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Model-tier")
public class CheckersGameTest {

    private Player redPlayer;
    private Player whitePlayer;
    private CheckersGame game;
    @BeforeEach
    public void setup(){
        redPlayer = new Player("red");
        whitePlayer = new Player("white");
        game = new CheckersGame(redPlayer, whitePlayer);
    }
    @Test
    public void test1(){
        assertTrue(game.getTurn().getPlayer().equals(game.getPlayerRed()));
    }

    @Test
    public void test2(){
        assertTrue(game.getPlayerColor(game.getPlayerRed()).equals(Piece.Color.RED) && game.getPlayerColor(game.getPlayerWhite()).equals(Piece.Color.WHITE));
    }

    @Test
    public void test4(){
        assertTrue(game.getPlayerActive().equals(game.getPlayerRed()));
    }

    @Test
    public void test5(){
        assertNull(game.getPlayerColor(new Player("test")));
    }

    @Test
    public void test6(){
        Turn mockTurn = game.getTurn();
        game.getTurn().validateMove(new Move(new Position(2,1), new Position(3,2)));
        game.getTurn().submitTurn();
        game.nextTurn();
        game.getTurn().validateMove(new Move(new Position(1,2), new Position(3,0)));
        game.getTurn().submitTurn();
        game.nextTurn();
        game.getTurn().validateMove(new Move(new Position(3,4), new Position(4,5)));
        game.getTurn().submitTurn();
        game.nextTurn();
        game.getTurn().validateMove(new Move(new Position(7,2), new Position(6,3)));
        game.getTurn().submitTurn();
        game.nextTurn();
        assertTrue(game.getTurn().getPlayer().equals(game.getPlayerRed()));
    }
}
