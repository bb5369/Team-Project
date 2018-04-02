package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

@Tag("Util-tier")
public class DoublyLinkedQueueTest {

    String three = "three";
    String two = "two";
    String one = "one";

    @Test
    public void testSize()
    {
        assertEquals(0, (new DoublyLinkedQueue()).size());
    }

    @Test
    public void testAddToFront()
    {
        final DoublyLinkedQueue<String> list = new DoublyLinkedQueue<>();
        list.addToFront(one);
        list.addToFront(two);
        assertEquals(two, list.removeFromFront());
    }

    @Test
    public void testAddToRear()
    {
        final DoublyLinkedQueue<String> list = new DoublyLinkedQueue<>();
        list.addToRear(one);
        list.addToRear(two);
        assertEquals(two, list.removeFromRear());
    }

    @Test
    public void testEnqueue()
    {
        final DoublyLinkedQueue<String> list = new DoublyLinkedQueue<>();
        list.enqueue(one);
        list.enqueue(two);
        assertEquals(one, list.dequeue());
    }

    @Test
    public void testDequeue()
    {
        assertThrows(NoSuchElementException.class, () -> {
            (new DoublyLinkedQueue()).dequeue();
        });

        DoublyLinkedQueue<String> list = new DoublyLinkedQueue();
        String two = "two";
        list.enqueue(two);
        assertEquals(two, list.dequeue());
    }

    @Test
    public void testRemoveFromRear()
    {
        assertThrows(NoSuchElementException.class, () -> {
            (new DoublyLinkedQueue()).removeFromRear();
        });
    }

    @Test
    public void testToString()
    {
        DoublyLinkedQueue<String> list = new DoublyLinkedQueue();
        list.enqueue(one);
        assertEquals("",(new DoublyLinkedQueue().toString()));
        assertEquals(one + "\n",list.toString());
    }

}
