package it.nerr.wolframalpha4j.route;

import io.netty.handler.codec.http.HttpMethod;
import it.nerr.wolframalpha4j.request.WebRequest;
import reactor.util.annotation.Nullable;

import java.util.Objects;

public class Route {

    private final HttpMethod method;
    private final String uriTemplate;

    private Route(HttpMethod method, String uriTemplate) {
        this.method = method;
        this.uriTemplate = uriTemplate;
    }

    public static Route get(String uri) {
        return new Route(HttpMethod.GET, uri);
    }

    public static Route post(String uri) {
        return new Route(HttpMethod.POST, uri);
    }

    public static Route put(String uri) {
        return new Route(HttpMethod.PUT, uri);
    }

    public static Route patch(String uri) {
        return new Route(HttpMethod.PATCH, uri);
    }

    public static Route delete(String uri) {
        return new Route(HttpMethod.DELETE, uri);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public WebRequest newRequest(Object... uriVars) {
        return new WebRequest(this, uriVars);
    }

    public String getUriTemplate() {
        return uriTemplate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, uriTemplate);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().isAssignableFrom(Route.class)) {
            return false;
        }
        Route other = (Route) obj;
        return other.method.equals(method) && other.uriTemplate.equals(uriTemplate);
    }

    @Override
    public String toString() {
        return "Route{method=" + method + ", uriTemplate='" + uriTemplate + "'}";
    }
}
