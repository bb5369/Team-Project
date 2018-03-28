package com.webcheckers.model;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Title: BoardView class</p>
 * <p>Description: This class represents a checkers board</p></p>
 */
public class BoardView implements Iterable{

    //instance variables
    private Row[] rows;
    private Space[][] spaceMatrix;
    private Space[][] reverseMatrix;

    /**
     * default constructor
     * This initializes each row in the rows array
     */
    public BoardView(){
        rows = new Row[8];
        spaceMatrix = new Space[8][8];
        reverseMatrix = new Space[8][8];
        for(int i = 0; i < rows.length; i++)
        {
            rows[i] = new Row(i);
            Iterator spaceIterator = rows[i].iterator();

            for(int j = 0; spaceIterator.hasNext(); j++)
            {
                spaceMatrix[i][j] = (Space)spaceIterator.next();
                reverseMatrix[7-i][7-j] = spaceMatrix[i][j];
            }
        }
    }

    public BoardView getReverseBoard(){
        BoardView b = new BoardView();
        for(int x = 0; x < 8; x++){
            b.rows[x] = rows[7-x].getReverseRow(7-x);
        }
        return b;
    }

    public Space[][] getSpaceMatrix()
    {
        return spaceMatrix;
    }

    public Space[][] getReverseMatrix()
    {
        return reverseMatrix;
    }

    public Space getSpace(int row, int col)
    {
        return spaceMatrix[row][col];
    }

    public Space getReverseSpace(int row,int col)
    {
        return reverseMatrix[col][row];
    }


    @Override
    public Iterator iterator() {
        return new RowIterator(rows);
    }

    /**
     * <p>Title: RowIterator</p>
     * <p>Description: This is implementation of Row iterator</p>
     */
    class RowIterator implements Iterator<Row> {

        private Row rows[] = new Row[8];

        public RowIterator(Row rows[]) {
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
        public Row next() {
            if (!hasNext()) {
                //System.out.println("**************************************Test: something must be wrong with the logic here");

            }
           // System.out.println("**************current value: " +current);
            return rows[current++];
        }
    }
}
