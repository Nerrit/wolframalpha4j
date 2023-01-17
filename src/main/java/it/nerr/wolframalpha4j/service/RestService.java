package it.nerr.wolframalpha4j.service;

import it.nerr.wolframalpha4j.route.Router;

public abstract class RestService {

    private final Router router;

    protected RestService(Router router) {
        this.router = router;
    }

    protected Router getRouter() {
        return router;
    }
}
