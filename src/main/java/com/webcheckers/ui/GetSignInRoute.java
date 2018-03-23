package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import spark.Route;
import spark.TemplateEngine;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * @author Justin Palmer
 */
public class GetSignInRoute implements Route {
    // TODO: Redirect to home if player session exists

    // Values used in the view-model map for rendering the sign-in view.
    static final String TITLE_ATTR = "title";
    static final String TITLE = "Player Sign-In";
    static final String VIEW_NAME = "signin.ftl";

    private final TemplateEngine templateEngine;

    GetSignInRoute(final TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
    }

    @Override
    public Object handle(Request request, Response response) {

        Map<String, Object> vm = new HashMap<>();

        vm.put(TITLE_ATTR, TITLE);

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
