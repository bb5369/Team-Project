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
 * UI Controller to GET the signin page
 */
public class GetSignInRoute implements Route {
    // TODO: Redirect to home if player session exists

    // Values used in the view-model map for rendering the sign-in view.
    static final String TITLE_ATTR = "title";
    static final String TITLE = "Player Sign-In";
    static final String VIEW_NAME = "signin.ftl";

    private final TemplateEngine templateEngine;

    /**
     * Initializes the GetSignInRoute
     *
     * @param templateEngine - template engine used to render view model
     */
    GetSignInRoute(final TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");

        this.templateEngine = templateEngine;
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

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
