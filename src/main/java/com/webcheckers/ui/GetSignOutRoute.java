package com.webcheckers.ui;


import spark.*;

import java.util.HashMap;
import java.util.Map;
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
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", TITLE);
        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
