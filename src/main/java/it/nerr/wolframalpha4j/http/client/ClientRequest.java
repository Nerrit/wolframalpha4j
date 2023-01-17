package it.nerr.wolframalpha4j.http.client;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import it.nerr.wolframalpha4j.request.WebRequest;
import it.nerr.wolframalpha4j.route.Route;
import it.nerr.wolframalpha4j.util.RouteUtils;
import reactor.util.annotation.Nullable;

import java.util.Optional;

public class ClientRequest {

    private final String id;
    private final WebRequest request;
    private final String url;
    private final HttpHeaders headers;
    private final Object body;

    /**
     * Create a new {@link ClientRequest} from the given request template.
     *
     * @param request the {@link WebRequest} template
     */
    public ClientRequest(WebRequest request) {
        this.request = request;
        this.url = RouteUtils.expandQuery(request.getCompleteUri(), request.getQueryParams());
        this.headers = Optional.ofNullable(request.getHeaders())
                .map(map -> map.entrySet().stream()
                        .reduce((HttpHeaders) new DefaultHttpHeaders(), (headers, entry) -> {
                            String key = entry.getKey();
                            entry.getValue().forEach(value -> headers.add(key, value));
                            return headers;
                        }, HttpHeaders::add))
                .orElse(new DefaultHttpHeaders());
        this.body = request.getBody();
        this.id = Integer.toHexString(System.identityHashCode(this));
    }

    /**
     * Return this request's ID for correlation.
     *
     * @return this request's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Return the HTTP method.
     *
     * @return the {@link HttpMethod} of this {@link ClientRequest}
     */
    public HttpMethod getMethod() {
        return request.getRoute().getMethod();
    }

    /**
     * Return the request URL.
     *
     * @return the request URL for this {@link ClientRequest}
     */
    public String getUrl() {
        return url;
    }

    /**
     * Return the headers of this request.
     *
     * @return the {@link HttpHeaders} of this {@link ClientRequest}
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    /**
     * Return the body to encode while processing this request.
     *
     * @return the request body, can be {@code null}
     */
    @Nullable
    public Object getBody() {
        return body;
    }

    /**
     * Return the original request template.
     *
     * @return the {@link WebRequest} template that created this {@link ClientRequest}
     */
    public WebRequest getWebRequest() {
        return request;
    }

    /**
     * Return the API endpoint targeted by this request.
     *
     * @return the {@link Route} requested by this {@link ClientRequest}
     */
    public Route getRoute() {
        return request.getRoute();
    }

    public String getDescription() {
        return request.getDescription();
    }

    @Override
    public String toString() {
        return "ClientRequest{" +
                "method=" + getMethod() +
                ", url='" + url + '\'' +
                ", headers=" + headers.copy().remove(HttpHeaderNames.AUTHORIZATION).toString() +
                ", body=" + body +
                ", id=" + id +
                '}';
    }
}
