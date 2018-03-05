package com.webcheckers.model;

import com.webcheckers.appl.RowIterator;

import java.util.Iterator;

/**
 * @author Alexis Halbur
 */
public class BoardView implements Iterable{

    private Row rows[] = new Row[8];

    public BoardView(){
    }

    @Override
    public Iterator iterator() {
        return new RowIterator(rows);
    }
}
