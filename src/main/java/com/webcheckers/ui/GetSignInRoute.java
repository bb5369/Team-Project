package com.webcheckers.ui;

import static spark.Spark.halt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.webcheckers.ui.GetHomeRoute;


import spark.*;

/**
 * @author Justin Palmer
 */
public class GetSignInRoute implements Route {

    // Values used in the view-model map for endering the singin view.
    static final String MSG = "Please Sign in";
    static final String TITLE = "Sign In Page";
    static final String VIEW_NAME = "signin.ftl";

    private final TemplateEngine templateEngine;

    GetSignInRoute(final TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = new HashMap<>();

        vm.put("title", "Player Sign-In");

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
