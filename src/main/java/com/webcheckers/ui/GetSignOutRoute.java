package com.webcheckers.ui;


import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.Objects;

public class GetSignOutRoute implements Route {

    static final String TITLE = "Player Sign Out";
    static final String VIEW_NAME = "signout.ftl";

    private final TemplateEngine templateEngine;

    GetSignOutRoute(final TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "Template Engine must not be null.");
        this.templateEngine = templateEngine;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
