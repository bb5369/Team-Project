package com.webcheckers.model;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Model-tier")
public class CheckersGameTest {
    private Player red, white;
    private CheckersGame game;
    @BeforeEach
    public void setUp(){
        red = new Player("red");
        white = new Player("White");
        game = new CheckersGame(red, white);
    }

    @Test
    public void players(){
        assertEquals(red, game.getPlayerActive());
        assertEquals(red, game.getPlayerRed());
        assertEquals(white, game.getPlayerWhite());
    }

}
