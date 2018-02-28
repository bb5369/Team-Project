package com.webcheckers.ui;

import static spark.Spark.halt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.webcheckers.ui.GetHomeRoute;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

/**
 * @author Justin Palmer
 */
public class GetSignInRoute implements Route {

    // Values used in the view-model map for endering the singin view.
    static final String MSG = "Please Sign in";
    static final String TITLE = "Sign In Page";
    static final String VIEW_NAME = "signin.ftl";

    private final TemplateEngine templateEngine;
    private final GetHomeRoute homeRoute;
    GetSignInRoute(final GetHomeRoute homeRoute, final TemplateEngine templateEngine){
        // validation
        Objects.requireNonNull(homeRoute, "homeRout must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.homeRoute = homeRoute;
        this.templateEngine = templateEngine;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
