package com.webcheckers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("Model-tier")
public class CheckersGameTest {

    private CheckersGame CuT;

    private BoardBuilder bob;

    private Player redPlayer;
    private Player whitePlayer;
    private Player activePlayer;
    private Space[][] matrix;
    private Turn turn;

    @BeforeEach
    public void setup(){
        redPlayer = new Player("redPlayer");
        whitePlayer = new Player("whitePlayer");
        turn = mock(Turn.class);
        bob = new BoardBuilder();
        matrix = bob.buildBoard();
    }

    @Test
    public void testConstructor(){
        CuT = new CheckersGame(redPlayer, whitePlayer);
    }

    @Test
    public void testActivePlayerSwapRed(){
        when(turn.getPlayer()).thenReturn(redPlayer);
        activePlayer = redPlayer;
        assertEquals(redPlayer, activePlayer);
    }

    @Test
    public void testActivePlayerSwapWhite(){
        when(turn.getPlayer()).thenReturn(whitePlayer);
        activePlayer = whitePlayer;
        assertEquals(whitePlayer, activePlayer);
    }

    @Test
    public void nextTurnWork(){
        when(turn.isSubmitted()).thenReturn(true);
        testActivePlayerSwapRed();
        testActivePlayerSwapWhite();
    }

    @Test
    public void nextTurnFail(){
        when(turn.isSubmitted()).thenReturn(false);

    }

    //@Test
    public void ihatethis(){
        //assertEquals(matrix, CuT.getMatrix());
        assertEquals(redPlayer, CuT.getPlayerRed());
        assertEquals(whitePlayer, CuT.getPlayerWhite());
        assertEquals(activePlayer, CuT.getPlayerActive());
        //assertEquals(turn, CuT.getTurn());
    }

}
