package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.http.ExchangeStrategies;
import it.nerr.wolframalpha4j.response.ResponseFunction;
import it.nerr.wolframalpha4j.util.ReactorResources;

import java.util.List;

public class RouterOptions {

    private final String token;
    private final ReactorResources reactorResources;
    private final ExchangeStrategies exchangeStrategies;
    private final List<ResponseFunction> responseTransformers;
    private final GlobalRateLimiter globalRateLimiter;
    private final RequestQueueFactory requestQueueFactory;
    private final String wolframAlphaApiBaseUrl;

    public RouterOptions(String token, ReactorResources reactorResources, ExchangeStrategies exchangeStrategies,
                         List<ResponseFunction> responseTransformers, GlobalRateLimiter globalRateLimiter,
                         RequestQueueFactory requestQueueFactory, String wolframAlphaApiBaseUrl) {
        this.token = token;
        this.reactorResources = reactorResources;
        this.exchangeStrategies = exchangeStrategies;
        this.responseTransformers = responseTransformers;
        this.globalRateLimiter = globalRateLimiter;
        this.requestQueueFactory = requestQueueFactory;
        this.wolframAlphaApiBaseUrl = wolframAlphaApiBaseUrl;
    }

    public String getToken() {
        return token;
    }

    public ReactorResources getReactorResources() {
        return reactorResources;
    }

    public ExchangeStrategies getExchangeStrategies() {
        return exchangeStrategies;
    }

    public List<ResponseFunction> getResponseTransformers() {
        return responseTransformers;
    }

    public GlobalRateLimiter getGlobalRateLimiter() {
        return globalRateLimiter;
    }

    public RequestQueueFactory getRequestQueueFactory() {
        return requestQueueFactory;
    }

    public String getWolframAlphaApiBaseUrl() {
        return wolframAlphaApiBaseUrl;
    }
}
