package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.appl.PlayerLobbyException;
import com.webcheckers.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import spark.TemplateEngine;
import spark.Route;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class PostSignInRoute implements Route {

    static final String TITLE = "Player Sign-In";
    static final String VIEW_NAME = "signin.ftl";

    static final String INVALID_NAME_TEXT = "Sorry, the name you've chosen is taken or invalid. " +
                                            "Please use spaces, letters, and numbers only.";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    PostSignInRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "templateEngine must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
    }

    /**
     * Accepts a player name input from the Sign-in form and attempts to create a new player
     * in the lobby. Passes on errors to the client UI, or moves the user to a signed-in homepage.
     * @param request
     * @param response
     * @return FreeMarker rendered template
     */
    @Override
    public Object handle(Request request, Response response) {
        // TODO: redirect to home if player session exists

        Map<String, Object> vm = new HashMap<>();

        String playerName = request.queryParams("name");

        Player newPlayer;

        try {

            newPlayer = playerLobby.newPlayer(playerName);
			// Add the new Player's model to their session
			request.session().attribute("Player", newPlayer);

			// Redirect to the homepage, which should now render the player lobby
			response.redirect(WebServer.HOME_URL);

        } catch (PlayerLobbyException e) {
            vm.put("message", e.getMessage());
		}

        vm.put("title", TITLE);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
