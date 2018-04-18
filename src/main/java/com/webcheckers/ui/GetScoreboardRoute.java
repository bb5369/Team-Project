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

    public GetScoreboardRoute(TournamentScoreboard tournamentScoreboard, TemplateEngine templateEngine){
        Objects.requireNonNull(tournamentScoreboard, "tournamentScoreboard must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.tournamentScoreboard = tournamentScoreboard;
        this.templateEngine = templateEngine;
        LOG.info("GetScoreboardRoute initialized");
    }

    @Override
    public Object handle(Request request, Response response) {

        //response.redirect(WebServer.SCOREBOARD_URL);

        Map<String, Object> vm = new HashMap<>();

        vm.put(TITLE_ATTR, TITLE);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
