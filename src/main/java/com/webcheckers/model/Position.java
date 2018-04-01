package com.webcheckers.model;

public class Position {

	//instance variables
	private int row;
	private int cell;

	/**
	 * pass in the row and cell to the constructor
	 * @param row
	 * @param cell
	 */
	Position(int row, int cell) {
		this.row = row;
		this.cell = cell;
	}

	/**
	 * getRow method--
	 * This method returns the x coordinate of the position
	 * @return row index
	 */
	public int getRow() {
		return this.row;
	}

	/**
	 * absoluteDifference method--
	 * This absolute difference of the end and start positions
	 * @param end
	 * @param start
	 * @return position with abosolute difference of the start and end positions
	 */
	public static Position absoluteDifference(Position end,Position start)
	{
		int x = Math.abs(end.row - start.row);
		int y = Math.abs(end.cell - start.cell);

		return new Position(x,y);
	}

	/**
	 * getCell method--
	 * This method returns the y coordinate of the position
	 * @return y coordinate of the position
	 */
	public int getCell() {
		return this.cell;
	}

	/**
	 * toString method--
	 * This method generates a string representing the state of the
	 * Position object
	 * @return - state of the object in the a string
	 */
	public String toString()
	{
		return new String("RowGen: " + row+ " Column: " + cell);
	}

}
