package com.webcheckers.model;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Title: Row class</p>
 * <p>Description: This class represents a row on a checkers board</p>
 */
public class Row implements Iterable {

	/**
	 * A "border" row is considered the last row a players pieces start in.
	 *
	 * Example board view:
	 * R0: WHITE
	 * R1: WHITE
	 * R2: WHITE
	 * R3: open
	 * R4: open
	 * R5: RED
	 * R6: RED
	 * R7: RED
	 *
	 * These constants are used in here in Row building and in Move validation for sanity
	 */
	public static final Integer WHITE_BORDER_INDEX = 2;
	public static final Integer RED_BORDER_INDEX   = 5;

    //instance variable
    private int index;
    private Space[] spaces;

    /**
     * parameterized constructor
     * This used to initialize the Row and each space in
     * a row by assigning a index
     * @param index space intex
     */
    public Row(int index){
        this.spaces = new Space[8];
		this.index = index;

		this.initializeSpaces();
    }


    private void initializeSpaces() {
        boolean startWhiteSquare = false;

		// Alternating rows begin with a dark space
        if(this.index % 2 == 0) {
            startWhiteSquare = true;
        }

        // Build row from Left to Right
		for (int i = 0; i < spaces.length; i++) {

        	if (startWhiteSquare) {
				// we use startWhiteSquare to indicate a square that player cannot land on
				spaces[i] = new Space(i);

			} else {
				// A player can land on this Space, or Space filled with starting Piece

				if (index <= WHITE_BORDER_INDEX) {
					spaces[i] =  new Space(i, new Piece(Piece.type.SINGLE, Piece.color.WHITE));

				} else if (index >= RED_BORDER_INDEX) {
					spaces[i] =  new Space(i, new Piece(Piece.type.SINGLE, Piece.color.RED));

				} else {
					spaces[i] = new Space(i, null);
				}

			}

			// The next Space is inverted, ie a White wont follow a Black
			startWhiteSquare = !startWhiteSquare;
		}
    }

    public Row getReverseRow(int index){
        Row r = new Row(index);
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
