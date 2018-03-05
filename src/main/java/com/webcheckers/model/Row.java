package com.webcheckers.model;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Team whatever
 */
public class Row implements Iterable{

    private int index;
    private Space[] spaces;

    public Row(int index){
        spaces = new Space[8];
        for(int i = 0; i < spaces.length; i++)
        {
            spaces[i] = new Space(i);
        }
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
        private Space spaces[];

        public SpaceIterator(Space spaces[]){
            this.spaces = spaces;
        }

        int current = 0;

        @Override
        public boolean hasNext() {
            if(current+1 < spaces.length)
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
