package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("Model-tier")
public class TournamentScoreBoardTest {
    private TournamentScoreboard CuT;

    private Player player;

    @BeforeEach
    public void setUp(){
        player = new Player("tourney", Player.GameType.TOURNAMENT);

        CuT = new TournamentScoreboard();

        CuT.newPlayer(player);
    }

    @Test
    public void expectedValues(){
        assertTrue(CuT.getPlayers().size() == 1);

    }

    @Test
    public void testOrder(){
        player = new Player("test", Player.GameType.TOURNAMENT);
        player.wonAGame();
        player.wonAGame();

        Player playerMid = new Player("mid", Player.GameType.TOURNAMENT);
        playerMid.wonAGame();

        CuT.newPlayer(player);
        CuT.newPlayer(playerMid);

        assertEquals(player, CuT.getPlayers().get(0));

        CuT.removePlayer(player);

        assertEquals(2, CuT.getPlayers().size());
    }
}
