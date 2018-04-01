package com.webcheckers.ui;

import static spark.Spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.google.gson.Gson;

import com.webcheckers.appl.GameManager;
import com.webcheckers.appl.PlayerLobby;
import spark.TemplateEngine;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for the
 * WebCheckers application.
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class WebServer {
	private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

	//
	// Constants
	//

	public static final String HOME_URL = "/";
	public static final String SIGNIN_URL = "/signin";
	public static final String GAME_URL = "/game";
	public static final String VALIDATE_MOVE_URL = "/validateMove";
	public static final String CLEAR_URL = "/clear";
	public static final String BACKUP_MOVE_URL = "/backupMove";
	public static final String SUBMIT_MOVE_URL = "/submitTurn";
	public static final String CHECK_TURN_URL = "/checkTurn";
	public static final String SIGNOUT_URL = "/signout";
	public static final String RESIGN_URL = "/resignGame";

	//
	// Attributes
	//

	private final TemplateEngine templateEngine;
	private final GameManager gameManager;
	private final PlayerLobby playerLobby;
	private final Gson gson;


	//
	// Constructor
	//

	/**
	 * The constructor for the Web Server.
	 *
	 * @param templateEngine The default {@link TemplateEngine} to render page-level HTML views.
	 * @param gson           The Google JSON parser object used to render Ajax responses.
	 * @throws NullPointerException If any of the parameters are {@code null}.
	 */
	public WebServer(final TemplateEngine templateEngine,
					 final GameManager gameManager,
					 final PlayerLobby playerLobby,
					 final Gson gson) {

		Objects.requireNonNull(templateEngine, "templateEngine must not be null");
		Objects.requireNonNull(gameManager, "gameManager must not be null");
		Objects.requireNonNull(playerLobby, "playerLobby must not be null");
		Objects.requireNonNull(gson, "gson must not be null");

		this.templateEngine = templateEngine;
		this.gameManager = gameManager;
		this.playerLobby = playerLobby;
		this.gson = gson;
	}

	//
	// Public methods
	//

	/**
	 * Initialize all of the HTTP routes that make up this web application.
	 * <p>
	 * <p>
	 * Initialization of the web server includes defining the location for static
	 * files, and defining all routes for processing client requests. The method
	 * returns after the web server finishes its initialization.
	 * </p>
	 */
	public void initialize() {

		// Configuration to serve static files
		staticFileLocation("/public");

		// Login and Player Lobby
		get(HOME_URL, new GetHomeRoute(templateEngine, playerLobby, gameManager));
		get(SIGNIN_URL, new GetSignInRoute(templateEngine));
		post(SIGNIN_URL, new PostSignInRoute(templateEngine, playerLobby));
		get(SIGNOUT_URL, new GetSignOutRoute(templateEngine, playerLobby, gameManager));

		// Game operation
		get(GAME_URL, new GetGameRoute(templateEngine, playerLobby, gameManager));
		post(VALIDATE_MOVE_URL, new PostValidateMoveRoute(gson, gameManager));
		post(BACKUP_MOVE_URL, new PostBackupMoveRoute(playerLobby, gameManager));
		post(SUBMIT_MOVE_URL, new PostSubmitTurnRoute(playerLobby, gameManager));
		post(CHECK_TURN_URL, new PostCheckTurnRoute(templateEngine, playerLobby, gameManager, gson));
		post(RESIGN_URL, new PostResignGameRoute(gameManager));

		// Admin functionality
		get(CLEAR_URL, new GetClearRoute(playerLobby, gameManager));

		LOG.config("WebServer is initialized.");
	}
}
