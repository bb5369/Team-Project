package com.webcheckers.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

@Tag("Model-tier")
public class MessageTest {

	private static final String MESSAGE_TEXT = "Unit testing is a chore";

	@Test
	public void test_infoMessage() {
		Message CuT = new Message(MESSAGE_TEXT, Message.MessageType.info);

		assertSame(CuT.getText(), MESSAGE_TEXT);
		assertSame(CuT.getType(), Message.MessageType.info);
	}

	@Test
	public void test_errorMessage() {
		Message CuT = new Message(MESSAGE_TEXT, Message.MessageType.error);

		assertSame(CuT.getText(), MESSAGE_TEXT);
		assertSame(CuT.getType(), Message.MessageType.error);
	}
}
