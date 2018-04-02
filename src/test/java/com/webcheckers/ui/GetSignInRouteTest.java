package com.webcheckers.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

@Tag("UI-tier")
public class GetSignInRouteTest {
    // The component-under-test (CuT).
    private GetSignInRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        CuT = new GetSignInRoute(engine);
    }
    @Test
    public void new_signIn(){
        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test (ignore the output)
        CuT.handle(request, response);

        // Analyze the content passed into the render method
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_ATTR, GetSignInRoute.TITLE);
        //   * test view name
        testHelper.assertViewName(GetSignInRoute.VIEW_NAME);
    }
    @Test
    public void faulty_session(){
        when(engine.render(any(ModelAndView.class))).thenReturn(null);

        try {
            CuT = new GetSignInRoute(engine);
            //expected
        }catch (IllegalArgumentException e){
            fail(e.getMessage());
        }
    }
    @Test
    public void faulty_session2() {
        // Arrange the test scenario: There is an existing session with a PlayerServices object
        when(request.attributes()).thenReturn(null);

        // Invoke the test
        try {
            CuT.handle(request, response);
            //expected
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }

        // Analyze the results:
        //   * redirect to the Game view
        //verify(response).redirect(WebServer.HOME_URL);
    }

}
