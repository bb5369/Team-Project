package com.webcheckers.appl;

import java.util.Iterator;

import com.webcheckers.model.Space;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Application-tier")
public class BoardViewGenTest {

    private static final Space[][] mockMatrix = new Space[8][8];
    private static BoardViewGen mockBoardView = new BoardViewGen(mockMatrix);
    private static Iterator BOARD_ITR;

    /**
     * Test that the constructor works.
     */
    @Test
    public void testConstructor(){
        new BoardViewGen(mockMatrix);
    }

    /**
     * Test that the iterator works properly
     */
    @Test
    public void testIterator(){
        BOARD_ITR = mockBoardView.iterator();
        while(BOARD_ITR.hasNext()) {
            BOARD_ITR.next();
        }
    }

    /**
     * Test that retrieving the reverse Board works
     */
    @Test
    public void testReverseBoard(){
        mockBoardView.getReverseBoard();
    }
}
