package com.webcheckers.appl;

import com.webcheckers.model.Space;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generates an Iterable Row of Spaces used by BoardViewGen to create a board view for the front end (UI)
 */
public class RowGen implements Iterable{

    //instance fields
    private int index;
    private Space[] spaces;

    public RowGen(int index, Space[] spaces){
        this.index = index;
        this.spaces = spaces;
    }

    /**
     * Reverses a row for the white player view
     * @param index: index of row to reverse
     * @return: a new reversed row
     */
    public RowGen getReverseRow(int index){
        Space[] newSpaces = new Space[8];
        RowGen r = new RowGen(index, newSpaces);
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
        return new SpaceIterator(spaces);
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
