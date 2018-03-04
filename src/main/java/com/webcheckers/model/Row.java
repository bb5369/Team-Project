package main.java.com.webcheckers.model;

import java.util.Iterator;

/**
 * @author Alexis Halbur
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
        return null;
    }

}
