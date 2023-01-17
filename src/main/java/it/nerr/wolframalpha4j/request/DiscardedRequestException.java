package it.nerr.wolframalpha4j.request;

/**
 * Thrown when a REST request is discarded because of a queue overflow.
 */
public class DiscardedRequestException extends RuntimeException {

    private final WebRequest request;

    public DiscardedRequestException(WebRequest request) {
        super("Request discarded due to backpressure: " + request.getDescription());
        this.request = request;
    }

    public WebRequest getRequest() {
        return request;
    }
}
