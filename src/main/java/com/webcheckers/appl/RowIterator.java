package com.webcheckers.appl;

import main.java.com.webcheckers.model.Row;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Alexis Halbur
 */
public class RowIterator implements Iterator<Row> {

    private Row rows[] = new Row[8];

    public RowIterator(Row rows[]){
        this.rows = rows;
    }

    int current = 0;

    @Override
    public boolean hasNext() {
        if(current < rows.length)
            return true;
        return false;
    }

    @Override
    public Row next() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        return rows[current++];
    }
}
