package it.nerr.wolframalpha4j.response;

import it.nerr.wolframalpha4j.http.client.ClientResponse;
import it.nerr.wolframalpha4j.request.RouteMatcher;
import it.nerr.wolframalpha4j.request.WebRequest;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

public class EmptyResponseTransformer implements ResponseFunction {

    private final RouteMatcher routeMatcher;
    private final Predicate<Throwable> predicate;

    public EmptyResponseTransformer(RouteMatcher routeMatcher, Predicate<Throwable> predicate) {
        this.routeMatcher = routeMatcher;
        this.predicate = predicate;
    }

    @Override
    public Function<Mono<ClientResponse>, Mono<ClientResponse>> transform(WebRequest request) {
        if (routeMatcher.matches(request)) {
            return mono -> mono.onErrorResume(predicate, t -> Mono.empty());
        }
        return mono -> mono;
    }
}
