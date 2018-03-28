package com.webcheckers.ui;

import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Alexis Halbur
 */
public class GetSignOutRoute implements Route {

    // Values used in view model to render sign out page
    static final String TITLE = "Player Sign Out";
    static final String VIEW_NAME = "signout.ftl";

    private final TemplateEngine templateEngine;

    /**
     * @param templateEngine
     */
    GetSignOutRoute(final TemplateEngine templateEngine){
        Objects.requireNonNull(templateEngine, "Template Engine must not be null.");
        this.templateEngine = templateEngine;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", TITLE);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
