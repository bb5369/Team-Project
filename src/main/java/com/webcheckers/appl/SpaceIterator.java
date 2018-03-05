package com.webcheckers.appl;

import com.webcheckers.model.Space;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Alexis Halbur
 */
public class SpaceIterator implements Iterator<Space> {
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
