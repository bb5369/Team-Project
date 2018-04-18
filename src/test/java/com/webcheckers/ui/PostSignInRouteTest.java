package com.webcheckers.ui;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.webcheckers.appl.PlayerLobbyException;
import com.webcheckers.model.TournamentScoreboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.*;

import com.webcheckers.model.Player;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerLobbyException;

import java.lang.reflect.Executable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for {@link PostSignInRoute} component.
 */
@Tag("UI-tier")
public class PostSignInRouteTest {

	private static final String PLAYER1_NAME = "Ian";
	private static final String PLAYER2_NAME = "Bob";

	private static final String PLAYER_LOBBY_EXCEPTION = "This is the exception";

	/**
	 * The component-under-test (CuT).
	 *
	 * <p>
	 * This is a stateless component so we only need one.
	 * The {@link PlayerLobby} component is not yet thoroughly tested
	 * so we must mock it.
	 */
	private PostSignInRoute CuT;

	// friendly objects
	private Player player1;
	private Player player2;

	// attributes holding mock objects
	private Request request;
	private Session session;
	private Response response;
	private TemplateEngine engine;
	private PlayerLobby playerLobby;
	private PlayerLobbyException playerLobbyException;
	private TournamentScoreboard tournamentScoreboard;

	/**
	 * Setup new mock objects for each test.
	 */
	@BeforeEach
	public void setup() {
		// external mocks
		request = mock(Request.class);
		session = mock(Session.class);
		engine = mock(TemplateEngine.class);
		response = mock(Response.class);

		// internal mocks
		playerLobby = mock(PlayerLobby.class);
		playerLobbyException = mock(PlayerLobbyException.class);
		tournamentScoreboard = mock(TournamentScoreboard.class);

		// build model objects
		player1 = new Player(PLAYER1_NAME, Player.GameType.NORMAL);
		player2 = new Player(PLAYER2_NAME, Player.GameType.NORMAL);

		// mock behavior
		when(request.session()).thenReturn(session);

		// create a unique CuT for each test
		CuT = new PostSignInRoute(engine, playerLobby, tournamentScoreboard);
	}

	/**
	 * Signing in should result in a redirect to the homepage
	 */
	@Test
	public void signInValidPlayerCasual() {

		when(session.attribute("Player")).thenReturn(player1);
		when(request.queryParams("name")).thenReturn(PLAYER1_NAME);
		when(request.queryParams("casual")).thenReturn("casual");
		when(session.attribute("player")).thenReturn(player1);
		when(request.session()).thenReturn(session);
		when(playerLobby.newPlayer(PLAYER1_NAME, Player.GameType.NORMAL)).thenReturn(player1);

		CuT.handle(request, response);

		verify(response, times(1)).redirect(WebServer.HOME_URL);
	}

	@Test
	@Disabled
	public void signInValidPlayerTournament() {

		when(session.attribute("Player")).thenReturn(player1);
		when(request.queryParams("name")).thenReturn(PLAYER1_NAME);
		when(playerLobby.newPlayer(PLAYER1_NAME, Player.GameType.TOURNAMENT)).thenReturn(player1);
		when(request.queryParams("casual")).thenReturn(null);
		when(session.attribute("player")).thenReturn(player1);
		when(request.session()).thenReturn(session);

		CuT.handle(request, response);

		verify(response, times(1)).redirect(WebServer.HOME_URL);
	}

	/**
	 * This tests the scenario when the PostSignInRoute has to catch an exception thrown by
	 * PlayerLobby because of an invalid name or otherwise.
	 */
	@Test
	@Disabled
	public void signInInvalidPlayer() {

		TemplateEngineTester testHelper = new TemplateEngineTester();


		when(playerLobbyException.getMessage()).thenReturn(PLAYER_LOBBY_EXCEPTION);

		when(request.queryParams("name")).thenReturn(PLAYER1_NAME);
		when(playerLobby.newPlayer(PLAYER1_NAME, Player.GameType.NORMAL)).thenThrow(playerLobbyException);

		when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

		CuT.handle(request, response);

		testHelper.assertViewModelAttribute("message", PLAYER_LOBBY_EXCEPTION);
	}
}
