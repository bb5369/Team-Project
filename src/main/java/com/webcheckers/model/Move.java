package com.webcheckers.model;

public class Move {
	//instance variable
	private Position start;
	private Position end;

	/**
	 * param constructor take in the start and end position
	 * @param start - start position of the move
	 * @param end - end position of the move
	 */
	Move(Position start, Position end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * getStart method--
	 * Used to access start position of the move
	 * @return start position of the move
	 */
	public Position getStart() {
		return this.start;
	}

	/**
	 * getEnd method--
	 * Used to access end position og the move
	 * @return end position of the move
	 */
	public Position getEnd() {
		return this.end;
	}

	/**
	 * getStartRow method--
	 * to access the x coordinate of the start position
	 * @return x coordinate of the start position
	 */
	public int getStartRow()
	{
		return start.getRow();
	}

	/**
	 * getStartCell method--
	 * Used to access the y coordinate of the start position
	 * @return
	 */
	public int getStartCell()
	{
		return start.getCell();
	}


	/**
	 * getEndRow method--
	 * Used to access the x coordinate of the end position
	 * @return x coordinate of the end position
	 */
	public int getEndRow()
	{
		return end.getRow();
	}

	/**
	 * getEndCell method--
	 * used to access the y coordinate of the end position
	 * @return y coordinate of the end position
	 */
	public int getEndCell()
	{
		return end.getCell();
	}

	/**
	 * isAJumpMoveAttempt method--
	 * This determine if a jump move was attempted
	 * @return true if the jump move was attempted
	 */
	public boolean isAJumpMoveAttempt()
	{
		Position diff = Position.absoluteDifference(end, start);
		return diff.getCell() > 1 || diff.getRow() > 1;
	}

	/**
	 * toString method--
	 * This generates a string representing the state of the Move object
	 * @return string representing the state of the move object
	 */
	public String toString()
	{
		int startX, startY, endX, endY;

        startX = start.getCell();
        startY = start.getRow();

        endX = end.getCell();
        endY = end.getRow();

        return String.format("<%d,%d> to <%d,%d>",
                startX, startY,
                endX, endY);
	}
}
