package com.webcheckers.ui;

import com.webcheckers.model.TournamentScoreboard;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


public class GetScoreboardRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetScoreboardRoute.class.getName());

    static final String TITLE_ATTR = "title";
    static final String TITLE = "Tournament Scoreboard";
    static final String VIEW_NAME = "scoreboard.ftl";

    private final TournamentScoreboard tournamentScoreboard;
    private final TemplateEngine templateEngine;

    /**
     * Initializes the GetScoreboardRoute
     *
     * @param tournamentScoreboard - scoreboard data
     * @param templateEngine - used to render the view model
     */
    public GetScoreboardRoute(TournamentScoreboard tournamentScoreboard, TemplateEngine templateEngine){
        Objects.requireNonNull(tournamentScoreboard, "tournamentScoreboard must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.tournamentScoreboard = tournamentScoreboard;
        this.templateEngine = templateEngine;
        LOG.info("GetScoreboardRoute initialized");
    }

    /**
     * Creates and renders the view model
     *
     * @param request  - the HTTP request
     * @param response - the HTTP response
     * @return - view model map
     */
    @Override
    public Object handle(Request request, Response response) {

        Map<String, Object> vm = new HashMap<>();

        vm.put(TITLE_ATTR, TITLE);
        vm.put("players", tournamentScoreboard.getPlayers().iterator());

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
