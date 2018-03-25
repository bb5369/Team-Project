package com.webcheckers.model;

import java.util.Objects;

/**
 * Model entity of a checkers Player
 */
public class Player {

	//instance variable
	public final String name;

	private PlayerState state;

	/**
	 * Parameterize constructor
	 * This constructor intializes the name of the player
	 * @param name: Player name String
	 */
	public Player(String name) {
		this.name = name;
	}

	public Player(String name, PlayerState state){
		this.name = name;
		this.state = state;
	}

	/**
	 * getName method
	 * This method is used to get player's name
	 * @return a string containing player's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * equals method
	 * This method is used to compare two Player objects
	 * @param obj other Player object to compare to
	 * @return true if both the Player have same name
	 */
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Player)) return false;

	  	Player otherPlayer = (Player) obj;

		return (this.name).equals(otherPlayer.getName());
	}

	public boolean checkState(PlayerState playerState) {
		return this.state == playerState;
	}

	public Player changeStae(Player player, PlayerState newState){
		Player newPlayerState = new Player(player.getName(), newState);
		return newPlayerState;
	}
	/**
	 * hashCode method
	 * This generates Player object's hashcode
	 * @return int value representing object's hashcode
	 */
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
