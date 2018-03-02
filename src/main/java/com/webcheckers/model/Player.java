package com.webcheckers.model;

import java.util.Objects;

/**
 * Model entity of a checkers Player
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

	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Player)) return false;

	  	Player otherPlayer = (Player) obj;

		return (this.name).equals(otherPlayer.getName());
	}

	public int hashCode() {
		return Objects.hash(
				this.name
		);
	}
}
