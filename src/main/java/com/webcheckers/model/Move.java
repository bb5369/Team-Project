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
}
