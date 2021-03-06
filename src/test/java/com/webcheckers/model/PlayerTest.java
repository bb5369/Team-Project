package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class PlayerTest {

    Player player = new Player("Bob", Player.GameType.NORMAL);

    @Test
    public void test1(){
        final Player bob = new Player("Bob", Player.GameType.NORMAL);
        assertTrue(bob.equals(player));
    }
    @Test
    public void test2(){
        assertFalse(player.equals(new Object()));
    }

    @Test
    public void test3(){
        String name = player.getName();
        assertEquals(player.hashCode(), new Player(name, Player.GameType.NORMAL).hashCode());
    }

    @Test
    public void test4(){
        assertTrue(player.equals(player));
    }

    @Test
    public void test5(){
        assertEquals(player.toString(), "Player Name: " + player.getName());
    }
}
