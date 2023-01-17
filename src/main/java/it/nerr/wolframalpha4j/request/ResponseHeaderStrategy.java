package it.nerr.wolframalpha4j.request;

import io.netty.handler.codec.http.HttpHeaders;
import reactor.netty.http.client.HttpClientResponse;

import java.time.Duration;

public class ResponseHeaderStrategy implements RateLimitStrategy {

    @Override
    public Duration apply(HttpClientResponse response) {
        HttpHeaders headers = response.responseHeaders();
        int remaining = headers.getInt("X-RateLimit-Remaining", -1);
        if (remaining == 0) {
            long resetAfter = (long) (Double.parseDouble(headers.get("X-RateLimit-Reset-After")) * 1000);
            return Duration.ofMillis(resetAfter);
        }
        return Duration.ZERO;
    }
}
