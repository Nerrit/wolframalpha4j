package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.route.Route;
import reactor.util.annotation.Nullable;

import java.util.Map;
import java.util.function.Predicate;

public class RouteMatcher {

    @Nullable
    private final WebRequest request;

    @Nullable
    private final Predicate<Map<String, String>> requestVariableMatcher;

    private RouteMatcher(@Nullable WebRequest request) {
        this(request, null);
    }

    public RouteMatcher(@Nullable WebRequest request, @Nullable Predicate<Map<String, String>> requestVariableMatcher) {
        this.request = request;
        this.requestVariableMatcher = requestVariableMatcher;
    }

    public static RouteMatcher any() {
        return new RouteMatcher(null);
    }

    public static RouteMatcher route(Route route) {
        return new RouteMatcher(route.newRequest());
    }

    public static RouteMatcher route(Route route, Predicate<Map<String, String>> requestVariableMatcher) {
        return new RouteMatcher(route.newRequest(), requestVariableMatcher);
    }

    public boolean matches(WebRequest otherRequest) {
        return matchesRoute(otherRequest) && matchesVariables(otherRequest);
    }

    private boolean matchesRoute(WebRequest otherRequest) {
        return request == null || request.getRoute().equals(otherRequest.getRoute());
    }

    private boolean matchesVariables(WebRequest otherRequest) {
        return requestVariableMatcher == null || otherRequest.matchesVariables(requestVariableMatcher);
    }
}
