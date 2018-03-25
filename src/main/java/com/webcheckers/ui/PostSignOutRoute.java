package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostSignOutRoute implements Route{

    static final String TITLE = "Player Sign-Out";
    static final String VIEW_NAME = "signout.ftl";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    public PostSignOutRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby){
        Objects.requireNonNull(templateEngine, "Template Engine must not be null.");
        Objects.requireNonNull(playerLobby, "Player Lobby must not be null");

        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = new HashMap<>();

        String playerName = request.queryParams("name");

        Player leavingPlayer = playerLobby.getPlayer(playerName);
        playerLobby.destroyPlayer(playerName);

        request.session().removeAttribute(playerName);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
