package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Alexis Halbur
 */
public class GetSignOutRoute implements Route {

    // Values used in view model to render sign out page
    static final String TITLE = "Player Sign-Out";
    static final String VIEW_NAME = "signout.ftl";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * @param templateEngine
     */
    GetSignOutRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby){
        Objects.requireNonNull(templateEngine, "Template Engine must not be null.");
        Objects.requireNonNull(playerLobby, "Player Lobby must not be null");
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
    }

    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();

        //String playerName = request.session().attribute("Player");

        Player player = request.session().attribute("Player");
        String playerName = player.getName();

        playerLobby.destroyPlayer(playerName);

        if ( ! playerLobby.isPlayerInLobby(player)) {
            // Remove the player from the session
            request.session().removeAttribute(playerName);
            player = null;
        }
        // Redirect to homepage which should show the Signed Out page
        response.redirect(WebServer.HOME_URL);

        vm.put("title", TITLE);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
