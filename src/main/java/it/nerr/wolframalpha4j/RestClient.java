package it.nerr.wolframalpha4j;

import it.nerr.wolframalpha4j.request.RouterOptions;
import it.nerr.wolframalpha4j.route.Router;
import it.nerr.wolframalpha4j.service.FullResultsService;
import it.nerr.wolframalpha4j.service.ShortAnswerService;
import it.nerr.wolframalpha4j.service.SimpleService;

public class RestClient {

    private final RestResources restResources;
    private final ShortAnswerService shortAnswerService;
    private final SimpleService simpleService;
    private final FullResultsService fullResultsService;

    /**
     * Create a {@link RestClient} with default options, using the given token for authentication.
     *
     * @param token the bot token used for authentication
     * @return a {@link RestClient} configured with the default options
     */
    public static RestClient create(String token) {
        return RestClientBuilder.createRest(token).build();
    }

    /**
     * Obtain a {@link RestClientBuilder} able to create {@link RestClient} instances, using the given token for
     * authentication.
     *
     * @param token the bot token used for authentication
     * @return a {@link RestClientBuilder}
     */
    public static RestClientBuilder<RestClient, RouterOptions> restBuilder(String token) {
        return RestClientBuilder.createRest(token);
    }

    /**
     * Create a new {@link RestClient} using the given {@link Router} as connector to perform requests.
     *
     * @param restResources a set of REST API resources required to operate this client
     */
    protected RestClient(final RestResources restResources) {
        this.restResources = restResources;
        final Router router = restResources.getRouter();
        this.shortAnswerService = new ShortAnswerService(router);
        this.simpleService = new SimpleService(router);
        this.fullResultsService = new FullResultsService(router);
    }

    /**
     * Obtain the {@link RestResources} associated with this {@link RestClient}.
     *
     * @return the current {@link RestResources} for this client
     */
    public RestResources getRestResources() {
        return restResources;
    }

    /**
     * Obtain the {@link ShortAnswerService} associated with this {@link RestClient}.
     *
     * @return the current {@link ShortAnswerService} for this client
     */
    public ShortAnswerService getShortAnswerService() {
        return shortAnswerService;
    }

    /**
     * Obtain the {@link SimpleService} associated with this {@link RestClient}.
     *
     * @return the current {@link SimpleService} for this client
     */
    public SimpleService getSimpleService() {
        return simpleService;
    }

    /**
     * Obtain the {@link FullResultsService} associated with this {@link RestClient}.
     *
     * @return the current {@link FullResultsService} for this client
     */
    public FullResultsService getFullResultsService() {
        return fullResultsService;
    }
}
