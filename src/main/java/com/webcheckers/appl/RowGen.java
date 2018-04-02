package com.webcheckers.appl;

import com.webcheckers.model.Space;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generates an Iterable Row of Spaces used by BoardViewGen to create a board view for the front end (UI)
 */
public class RowGen implements Iterable {

    //instance fields
    private int index;
    private Space[] spaces;

    /**
     * Parameterized constructor
     * Initializes a row of a certain index with an array of spaces
     *
     * @param index - index of the row
     * @param spaces - array of spaces to occupy the row
     */
    public RowGen(int index, Space[] spaces) {
        this.index = index;
        this.spaces = spaces;
    }

    /**
     * Reverses a row for the white player view
     *
     * @param index - index of row to reverse
     * @return - a new reversed row
     */
    public RowGen getReverseRow(int index) {
        Space[] newSpaces = new Space[8];
        RowGen r = new RowGen(index, newSpaces);
        for (int x = 0; x < 8; x++) {
            r.spaces[x] = spaces[7 - x];
        }
        return r;
    }

    /**
     * This is getter for index
     *
     * @return - int represent the index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns an iterator for the spaces
     *
     * @return - SpaceIterator to iterate over an array of spaces
     */
    @Override
    public Iterator iterator() {
        return new SpaceIterator(spaces);
    }

    /**
     * <p>Title: SpaceIterator</p>
     * <p>Description: This is implementation of a Space iterator</p>
     */
    class SpaceIterator implements Iterator<Space> {
        private Space spaces[];

        /**
         * Parameterized constructor
         *
         * @param spaces - array of spaces
         */
        public SpaceIterator(Space spaces[]) {
            this.spaces = spaces;
        }

        int current = 0;

        /**
         * Determines whether or not the iterator is at the end of the array
         *
         * @return - true if not the end of the spaces, false otherwise
         */
        @Override
        public boolean hasNext() {
            if (current < spaces.length)
                return true;
            return false;
        }

        /**
         * If there is next space, return it
         *
         * @return - the next space in the array
         */
        @Override
        public Space next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return spaces[current++];
        }
    }
}
