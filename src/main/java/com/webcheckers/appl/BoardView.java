package com.webcheckers.appl;

import com.webcheckers.model.Space;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generates the board view instance that the front end (UI) needs to display a board
 */
public class BoardView implements Iterable{

    private Space[][] matrix;
    private Row[] rows;

    public BoardView(Space[][] spaces){
        rows = new Row[8];
        matrix = spaces;
        for(int i = 0; i < rows.length; i++)
        {
            rows[i] = new Row(i, spaces[i]);
        }
    }

    public BoardView getReverseBoard(){
        BoardView b = new BoardView(matrix);
        for(int x = 0; x < 8; x++){
            b.rows[x] = rows[7-x].getReverseRow(7-x);
        }
        return b;
    }

    @Override
    public Iterator iterator() {
        return new BoardView.RowIterator(rows);
    }

    /**
     * <p>Title: RowIterator</p>
     * <p>Description: This is implementation of Row iterator</p>
     */
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
                //System.out.println("**************************************Test: something must be wrong with the logic here");
            }
            // System.out.println("**************current value: " +current);
            return rows[current++];
        }
    }









    /**
     * An iterable Row of Spaces used by BoardView to create a board view for the front end (UI)
     * TODO: decide whether or not to create separate class file for this
     */
    class Row implements Iterable{

        private int index;
        private Space[] spaces;

        public Row(int index, Space[] spaces){
            this.index = index;
            this.spaces = spaces;
        }

        public Row getReverseRow(int index){
            Row r = new Row(index, spaces);
            for(int x = 0; x < 8; x++){
                r.spaces[x] = spaces[7-x];
            }
            return r;
        }

        /**
         * getIndex method
         * This is getter for index
         * @return - int represent the index
         */
        public int getIndex(){
            return this.index;
        }

        @Override
        public Iterator iterator() {
            return new BoardView.Row.SpaceIterator(spaces);
        }

        class SpaceIterator implements Iterator<Space> {
            private Space spaces[];

            public SpaceIterator(Space spaces[]){
                this.spaces = spaces;
            }

            int current = 0;

            @Override
            public boolean hasNext() {
                if(current < spaces.length)
                    return true;
                return false;
            }

            @Override
            public Space next() {
                if(!hasNext()){
                    throw new NoSuchElementException();
                }
                return spaces[current++];
            }
        }
    }
}