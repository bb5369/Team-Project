package com.webcheckers.appl;

import com.webcheckers.model.Space;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generates the board view instance that the front end (UI) needs to display a board
 */
public class BoardViewGen implements Iterable{

    private Space[][] matrix;
    private RowGen[] rows;

    public BoardViewGen(Space[][] spaces){
        rows = new RowGen[8];
        matrix = spaces;
        for(int i = 0; i < rows.length; i++)
        {
            rows[i] = new RowGen(i, spaces[i]);
        }
    }

    public BoardViewGen getReverseBoard(){
        BoardViewGen b = new BoardViewGen(matrix);
        for(int x = 0; x < 8; x++){
            b.rows[x] = rows[7-x].getReverseRow(7-x);
        }
        return b;
    }

    @Override
    public Iterator iterator() {
        return new BoardViewGen.RowIterator(rows);
    }

    /**
     * <p>Title: RowIterator</p>
     * <p>Description: This is implementation of Row iterator</p>
     */
    class RowIterator implements Iterator<RowGen> {

        private RowGen rows[] = new RowGen[8];

        public RowIterator(RowGen rows[]) {
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
        public RowGen next() {
            if (!hasNext()) {
                //System.out.println("**************************************Test: something must be wrong with the logic here");
            }
            // System.out.println("**************current value: " +current);
            return rows[current++];
        }
    }


    /**
     * An iterable Row of Spaces used by BoardViewGen to create a board view for the front end (UI)
     * TODO: decide whether or not to create separate class file for this
     */
    class RowGen implements Iterable{

        private int index;
        private Space[] spaces;

        public RowGen(int index, Space[] spaces){
            this.index = index;
            this.spaces = spaces;
        }

        public RowGen getReverseRow(int index){
            RowGen r = new RowGen(index, spaces);
            for(int x = 0; x < 8; x++){
                r.spaces[x] = spaces[7-x];
            }
            return r;
        }

        @Override
        public Iterator iterator() {
            return new BoardViewGen.RowGen.SpaceIterator(spaces);
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