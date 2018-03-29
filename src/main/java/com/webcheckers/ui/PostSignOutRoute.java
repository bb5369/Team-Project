package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static com.webcheckers.ui.WebServer.HOME_URL;

/**
 * @author Alexis Halbur
 */
public class PostSignOutRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

    static final String TITLE = "Player Sign-Out";
    static final String VIEW_NAME = "signout.ftl";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * @param templateEngine    HTML template rendering engine
     * @param playerLobby       PlayerLobby component
     */
    public PostSignOutRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby){
        Objects.requireNonNull(templateEngine, "Template Engine must not be null.");
        Objects.requireNonNull(playerLobby, "Player Lobby must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
    }

    /**
     * Finds the player name of the player signing out, removes the player from the playerLobby,
     * removes the player from the session, and redirects to the signed out page.
     *
     * @param request
     * @param response
     * @return Freemarker rendered template
     */
    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();

        Player player = request.session().attribute("Player");
        String playerName = player.getName();
        LOG.info("Player Name: " + playerName);

        playerLobby.destroyPlayer(playerName);

        // Remove the player from the session
        request.session().removeAttribute(playerName);

        // Redirect to homepage which should show the Signed Out page
        response.redirect(HOME_URL);

        vm.put("title", TITLE);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
