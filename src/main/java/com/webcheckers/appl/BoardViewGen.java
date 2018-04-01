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

    /**
     * Reverses the board for the white player's view
     * @return: a new reversed board view
     */
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
     * <p>Description: This is implementation of RowGen iterator</p>
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
                throw new NoSuchElementException();
            }
            return rows[current++];
        }
    }
}