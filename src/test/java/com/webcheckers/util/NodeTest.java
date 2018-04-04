package com.webcheckers.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Util-tier")
public class NodeTest {
    private static final String CURRENT_NODE_DATA = "Current node";
    private static final String UPDATE_CURRENT_NODE_DATA = "Updated Current node";
    private static final String PREV_NODE_DATA = "Previous node";
    private static final String NEXT_NODE_DATA = "Next node";


    Node<String> next = new Node(NEXT_NODE_DATA, null, null);
    Node<String> prev = new Node(PREV_NODE_DATA, null, null);
    Node<String> current = new Node(CURRENT_NODE_DATA, next, prev );

    @Test
    public void testGetData()
    {
        final String data= current.getData();
        assertEquals(data,CURRENT_NODE_DATA);
    }

    @Test
    public void testSetData()
    {
        current.setData(UPDATE_CURRENT_NODE_DATA);
        final String updatedData = current.getData();
        assertEquals(updatedData, UPDATE_CURRENT_NODE_DATA);
    }

    @Test
    public void testGetNext()
    {
        assertEquals(next, current.getNext());
    }

    @Test
    public void testGetPrev()
    {
        assertEquals(prev, current.getPrev());
    }

    @Test
    public void testSetNext()
    {
        current.setNext(prev);
        assertEquals(current.getNext(),prev);
    }

    @Test
    public void testSetPrev()
    {
        current.setPrev(next);
        assertEquals(current.getPrev(),next);
    }
 }
