package com.webcheckers.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
public class PlayerTest {
    Player player = new Player("Bob");
    @Test
    public void test1(){
        final Player bob = new Player("Bob");
        assertTrue(bob.equals(player));
    }
    @Test
    public void test2(){
        final Player pal = new Player("Rob");
        assertFalse(pal.equals(player));
    }

    @Test
    public void test3(){
        String name = player.getName();
        assertEquals(player.hashCode(), new Player(name).hashCode());
    }
}
