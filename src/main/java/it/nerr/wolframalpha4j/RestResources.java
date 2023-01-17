package it.nerr.wolframalpha4j;

import it.nerr.wolframalpha4j.route.Router;
import it.nerr.wolframalpha4j.util.JacksonResources;
import it.nerr.wolframalpha4j.util.ReactorResources;
import it.nerr.wolframalpha4j.util.TokenUtil;

public class RestResources {

    private final String token;
    private final ReactorResources reactorResources;
    private final JacksonResources jacksonResources;
    private final Router router;
    private final String selfId;

    /**
     * Create a {@link RestResources} instance with the given resources.
     *
     * @param token the bot token used to authenticate requests
     * @param reactorResources Reactor resources to establish connections and schedule tasks
     * @param jacksonResources Jackson data-binding resources to map objects
     * @param router a connector to perform requests against Discord API
     */
    public RestResources(String token, ReactorResources reactorResources, JacksonResources jacksonResources,
                         Router router) {
        this.token = token;
        this.reactorResources = reactorResources;
        this.jacksonResources = jacksonResources;
        this.router = router;
        this.selfId = token;//TokenUtil.getSelfId(token);
    }

    /**
     * Return the bot token used to authenticate requests.
     *
     * @return the bot token
     */
    public String getToken() {
        return token;
    }

    /**
     * Return Reactor resources to establish connections and schedule tasks.
     *
     * @return a configured {@link ReactorResources} instance
     */
    public ReactorResources getReactorResources() {
        return reactorResources;
    }

    /**
     * Return Jackson resources to transform objects.
     *
     * @return a configured {@link JacksonResources} instance
     */
    public JacksonResources getJacksonResources() {
        return jacksonResources;
    }

    /**
     * Return the {@link Router} tied to this resources object.
     *
     * @return a configured {@link Router} instance
     */
    public Router getRouter() {
        return router;
    }
}
