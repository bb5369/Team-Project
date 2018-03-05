package com.webcheckers.model;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Team whatever
 */
public class Row implements Iterable{

    private int index;
    private Space spaces[] = new Space[8];

    public Row(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }

    @Override
    public Iterator iterator() {
        return new SpaceIterator(spaces);
    }

    class SpaceIterator implements Iterator<Space> {
        private Space spaces[] = new Space[8];

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
