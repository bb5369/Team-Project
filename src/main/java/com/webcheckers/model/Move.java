package com.webcheckers.model;

public class Move {
	private Position start;
	private Position end;

	Move(Position start, Position end) {
		this.start = start;
		this.end = end;
	}

	public Position getStart() {
		return this.start;
	}

	public Position getEnd() {
		return this.end;
	}

	public int getStartRow()
	{
		return start.getRow();
	}
	public int getStartCell()
	{
		return start.getCell();
	}


	public int getEndRow()
	{
		return end.getRow();
	}

	public int getEndCell()
	{
		return end.getCell();
	}

	public String toString()
	{
		return new String("Start" + start + " " + "End" + end);
	}
}
