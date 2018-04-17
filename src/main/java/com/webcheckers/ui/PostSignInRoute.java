package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerLobbyException;
import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.TemplateEngine;
import spark.Route;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * UI Controller for POST when signed in
 */
public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    static final String TITLE = "Player Sign-In";
    static final String VIEW_NAME = "signin.ftl";

    static final String INVALID_NAME_TEXT = "Sorry, the name you've chosen is taken or invalid. " +
            "Please use spaces, letters, and numbers only.";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * Initializes the PostSignInRoute
     *
     * @param templateEngine - template engine used to render the view model
     * @param playerLobby    - player lobby used to create and store a new player
     */
    PostSignInRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;

        LOG.config("PostSignInRoute is initialized");
    }

    /**
     * Accepts a player name input from the Sign-in form and attempts to create a new player
     * in the lobby. Passes on errors to the client UI, or moves the user to a signed-in homepage.
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - FreeMarker rendered template
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.fine("PostSignInRoute is invoked.");
        // TODO: redirect to home if player session exists

        Map<String, Object> vm = new HashMap<>();

        String playerName = request.queryParams("name");

        String casual = request.queryParams("casual");
        //String tournament = request.queryParams("tournament");

        Player.GameType type;

        if(casual == null) {
            type = Player.GameType.TOURNAMENT;
            LOG.info(String.format("Player [%s] is in TOURNAMENT mode.", playerName));
        }else {
            type = Player.GameType.NORMAL;
            LOG.info(String.format("Player [%s] is in NORMAL mode.", playerName));
        }

        Player newPlayer;

        try {

            newPlayer = playerLobby.newPlayer(playerName, type);
            // Add the new Player's model to their session
            request.session().attribute("Player", newPlayer);

            LOG.info(String.format("Player [%s] has signed in!", playerName));

            // Redirect to the homepage, which should now render the player lobby
            response.redirect(WebServer.HOME_URL);

        } catch (PlayerLobbyException e) {
            LOG.warning(e.getMessage());
            vm.put("message", e.getMessage());
        }

        vm.put("title", TITLE);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
