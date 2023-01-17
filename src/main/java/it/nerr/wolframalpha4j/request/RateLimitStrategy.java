package it.nerr.wolframalpha4j.request;

import reactor.netty.http.client.HttpClientResponse;

import java.time.Duration;

/**
 * A mapper between a {@link HttpClientResponse} and a {@link Duration} representing a delay due to rate limiting.
 */
@FunctionalInterface
public interface RateLimitStrategy {

    /**
     * Apply this function to a {@link HttpClientResponse} to obtain a {@link Duration} representing a delay due to
     * rate limiting.
     *
     * @param response the original {@link HttpClientResponse}
     * @return a {@link Duration} indicating rate limiting, can be {@link Duration#ZERO} if no limit is present
     */
    Duration apply(HttpClientResponse response);
}
