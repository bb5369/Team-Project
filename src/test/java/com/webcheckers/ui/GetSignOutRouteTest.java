package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class GetSignOutRouteTest {

    /**
     * Component under test
     */
    private GetSignOutRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;
    private TemplateEngineTester testHelper;
    private PlayerLobby playerLobby;

    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);
        testHelper = new TemplateEngineTester();

        CuT = new GetSignOutRoute(templateEngine, playerLobby);
    }

    @Test
    public void run() {
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", GetSignOutRoute.TITLE);
        testHelper.assertViewName(GetSignOutRoute.VIEW_NAME);
    }
;

}
