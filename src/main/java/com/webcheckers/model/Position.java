package com.webcheckers.model;

public class Position {
	private int row;
	private int cell;

	Position(int row, int cell) {
		this.row = row;
		this.cell = cell;
	}

	public int getRow() {
		return this.row;
	}

	public int getCell() {
		return this.cell;
	}

	public String toString()
	{
		return new String("RowGen: " + row+ " Column: " + cell);
	}

}
