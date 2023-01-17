package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.route.Route;
import it.nerr.wolframalpha4j.route.Router;
import it.nerr.wolframalpha4j.util.RouteUtils;
import reactor.util.annotation.Nullable;

import java.util.*;
import java.util.function.Predicate;

public class WebRequest {

    private final Route route;
    private final String completeUri;
    private final Map<String, String> uriVariableMap;

    @Nullable
    private Object body;

    @Nullable
    private Map<String, List<Object>> queryParams;

    @Nullable
    private Map<String, Set<String>> headers;

    public WebRequest(Route route, Object... uriVars) {
        this.route = route;
        this.completeUri = RouteUtils.expand(route.getUriTemplate(), uriVars);
        this.uriVariableMap = RouteUtils.createVariableMap(route.getUriTemplate(), uriVars);
    }

    public Route getRoute() {
        return route;
    }

    public String getCompleteUri() {
        return completeUri;
    }

    @Nullable
    public Object getBody() {
        return body;
    }

    @Nullable
    public Map<String, List<Object>> getQueryParams() {
        return queryParams;
    }

    @Nullable
    public Map<String, Set<String>> getHeaders() {
        return headers;
    }

    public WebRequest body(@Nullable Object body) {
        this.body = body;
        return this;
    }

    public WebRequest query(String key, Object value) {
        if (queryParams == null) {
            queryParams = new LinkedHashMap<>();
        }
        queryParams.computeIfAbsent(key, k -> new LinkedList<>()).add(value);
        return this;
    }

    public WebRequest queryList(Map<String, Object> params) {
        params.forEach(this::query);
        return this;
    }

    public WebRequest query(Map<String, List<Object>> params) {
        params.forEach((key, values) -> values.forEach(value -> query(key, value)));
        return this;
    }

    public WebRequest header(String key, String value) {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.computeIfAbsent(key.toLowerCase(), k -> new LinkedHashSet<>()).add(value);
        return this;
    }

    public WebRequest optionalHeader(String key, @Nullable String value) {
        return value == null ? this : header(key, value);
    }

    public boolean matchesVariables(Predicate<Map<String, String>> matcher) {
        return matcher.test(uriVariableMap);
    }

    public WebResponse exchange(Router router) {
        return router.exchange(this);
    }

    public String getDescription() {
        return route.getMethod() + " " + completeUri;
    }

    @Override
    public String toString() {
        return "WebRequest{" +
                "route=" + route +
                ", completeUri='" + completeUri + '\'' +
                ", body=" + body +
                ", queryParams=" + queryParams +
                ", headers=" + headers +
                '}';
    }
}
