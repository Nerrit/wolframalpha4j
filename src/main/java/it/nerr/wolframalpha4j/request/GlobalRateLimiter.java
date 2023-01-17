package it.nerr.wolframalpha4j.request;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface GlobalRateLimiter {

    static GlobalRateLimiter create() {
        return BucketGlobalRateLimiter.create();
    }

    Mono<Void> rateLimitFor(Duration duration);

    Mono<Duration> getRemaining();

    <T> Flux<T> withLimiter(Publisher<T> stage);
}
