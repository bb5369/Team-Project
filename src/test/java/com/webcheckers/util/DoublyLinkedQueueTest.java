package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Util-tier")
public class DoublyLinkedQueueTest {

    DoublyLinkedQueue<String> list = new DoublyLinkedQueue();

    @Test
    public void testSize()
    {
        assertEquals(0, (new DoublyLinkedQueue()).size());
    }

}
