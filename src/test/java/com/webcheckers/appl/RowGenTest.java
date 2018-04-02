package com.webcheckers.appl;

import com.webcheckers.model.Space;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

@Tag("Application-tier")
public class RowGenTest {

    private static final Space[] spaces = new Space[8];
    private static final int ROW_INDEX = 0;
    private static final RowGen ROW = new RowGen(0, spaces);
    private static final Iterator ROW_ITR = ROW.iterator();

    /**
     * Test that the constructor works.
     */
    @Test
    public void testConstructor(){
        new RowGen(ROW_INDEX, spaces);
    }

    /**
     * Test that the iterator works properly
     */
    @Test
    public void testIterator(){
        while(ROW_ITR.hasNext()){
            ROW_ITR.next();
        }
    }

    /**
     * Test that getReverseRow and getIndex work without failure
     */
    @Test
    public void testReverseRow(){
        ROW.getReverseRow(ROW.getIndex());
    }
}