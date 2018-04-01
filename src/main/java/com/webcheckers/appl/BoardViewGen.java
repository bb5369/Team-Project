package com.webcheckers.appl;

import com.webcheckers.model.Space;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generates the board view instance that the front end (UI) needs to display a board
 */
public class BoardViewGen implements Iterable {

    private Space[][] matrix;
    private RowGen[] rows;

    /**
     * Parameterized constructor
     * Initializes the matrix
     *
     * @param spaces - matrix of spaces
     */
    public BoardViewGen(Space[][] spaces) {
        rows = new RowGen[8];
        matrix = spaces;
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowGen(i, spaces[i]);
        }
    }

    /**
     * Reverses the board for the white player's view
     *
     * @return - a new reversed board view
     */
    public BoardViewGen getReverseBoard() {
        BoardViewGen b = new BoardViewGen(matrix);
        for (int x = 0; x < 8; x++) {
            b.rows[x] = rows[7 - x].getReverseRow(7 - x);
        }
        return b;
    }

    /**
     * Returns a RowIterator to iterate over the rows
     *
     * @return - RowIterator
     */
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

        /**
         * Determines whether or not the iterator is at the end of the line
         *
         * @return - true if not the end of the rows, false otherwise
         */
        @Override
        public boolean hasNext() {
            if (current < rows.length)
                return true;
            return false;
        }

        /**
         * If there is next space, return it
         *
         * @return - the next space in the row
         */
        @Override
        public RowGen next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return rows[current++];
        }
    }
}