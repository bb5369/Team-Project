package com.webcheckers.appl;

/**
 * <p>Title: PlayerLobbyException Class</p>
 * <p>Description: Exception used by the PLayerLobby class</p>
 */
public class PlayerLobbyException extends RuntimeException {
	/**
	 * parameterized constructor
	 * This instantiates a new instance of the PlayerLobbyException
	 * using the parents cont
	 * @param message - message passed to the Exception
	 */
	PlayerLobbyException(String message) {
		super(message);
	}
}
