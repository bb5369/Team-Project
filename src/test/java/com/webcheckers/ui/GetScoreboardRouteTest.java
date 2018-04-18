package com.webcheckers.ui;

import com.webcheckers.model.TournamentScoreboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class GetScoreboardRouteTest {

    private GetScoreboardRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private TournamentScoreboard tournamentScoreboard;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        tournamentScoreboard = mock(TournamentScoreboard.class);

        CuT = new GetScoreboardRoute(tournamentScoreboard, engine);
    }

    @Test
    @Disabled
    public void functional(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute(GetScoreboardRoute.TITLE_ATTR, GetScoreboardRoute.TITLE);
        testHelper.assertViewName(GetScoreboardRoute.VIEW_NAME);
    }

}
