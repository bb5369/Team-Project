package com.webcheckers.model;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Team whatever
 */
public class BoardView implements Iterable{

    private Row[] rows;

    public BoardView(){
        rows = new Row[8];
        for(int i = 0; i < rows.length; i++)
        {
            rows[i] = new Row(i);
        }
    }

    @Override
    public Iterator iterator() {
        return new RowIterator(rows);
    }

    class RowIterator implements Iterator<Row> {

        private Row rows[] = new Row[8];

        public RowIterator(Row rows[]) {
            this.rows = rows;
        }

        int current = 0;

        @Override
        public boolean hasNext() {
            if (current < rows.length)
                return true;
            return false;
        }

        @Override
        public Row next() {
            if (!hasNext()) {
                System.out.println("**************************************Test: something must be wrong with the logic here");

            }
            System.out.println("**************current value: " +current);
            return rows[current++];
        }
    }
}
