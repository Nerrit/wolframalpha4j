package it.nerr.wolframalpha4j.request;

import reactor.core.publisher.Sinks;
import reactor.util.context.ContextView;

public class RequestCorrelation<T> {

    private final WebRequest request;
    private final Sinks.One<T> response;
    private final ContextView context;

    RequestCorrelation(WebRequest request, Sinks.One<T> response, ContextView context) {
        this.request = request;
        this.response = response;
        this.context = context;
    }

    public WebRequest getRequest() {
        return request;
    }

    public Sinks.One<T> getResponse() {
        return response;
    }

    public ContextView getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "RequestCorrelation{" +
                "request=" + request.getDescription() +
                ", response=" + response +
                ", context=" + context +
                '}';
    }
}
