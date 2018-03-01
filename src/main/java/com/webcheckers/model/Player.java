package com.webcheckers.model;

/**
 * Model entity for a checkers Player
 */
public class Player {
	//instance variables
	private String name;
	
	/**
	 * Parameterize constructor
	 * This constructor intializes the name of the player
	 * @param name: Player name String
	 */
	public Player(String name) {
		this.name = name;
	}
	
	/**
	 * getName method
	 * This method is used to get player's name
	 * @return a string containing player's name.
	 */
	public String getName() {
		return this.name;
	}
}
