package com.webcheckers.appl;

public class PlayerLobbyException extends RuntimeException {

	private String message;

	PlayerLobbyException(String message) {
		this.message = message;
	}


	@Override
	public String getMessage() {
		return this.message;
	}
}
