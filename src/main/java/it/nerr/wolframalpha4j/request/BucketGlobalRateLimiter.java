package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.operator.RateLimitOperator;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;

public class BucketGlobalRateLimiter implements GlobalRateLimiter {

    private static final Logger LOGGER = Loggers.getLogger(BucketGlobalRateLimiter.class);

    private final RateLimitOperator<Integer> operator;

    private volatile long limitedUntil = 0;

    BucketGlobalRateLimiter(int capacity, Duration refillPeriod, Scheduler delayScheduler) {
        this.operator = new RateLimitOperator<>(capacity, refillPeriod, delayScheduler);
    }

    public static BucketGlobalRateLimiter create() {
        return new BucketGlobalRateLimiter(50, Duration.ofSeconds(1), Schedulers.parallel());
    }

    public static BucketGlobalRateLimiter create(int capacity, Duration refillPeriod, Scheduler delayScheduler) {
        return new BucketGlobalRateLimiter(capacity, refillPeriod, delayScheduler);
    }

    @Override
    public Mono<Void> rateLimitFor(Duration duration) {
        return Mono.fromRunnable(() -> limitedUntil = System.nanoTime() + duration.toNanos());
    }

    @Override
    public Mono<Duration> getRemaining() {
        return Mono.fromCallable(() -> Duration.ofNanos(limitedUntil - System.nanoTime()));
    }

    @Override
    public <T> Flux<T> withLimiter(Publisher<T> stage) {
        return Mono.just(0)
                .transform(operator)
                .then(getRemaining())
                .filter(delay -> delay.getSeconds() > 0)
                .flatMapMany(delay -> {
                    LOGGER.trace("[{}] Delaying for {}", Integer.toHexString(hashCode()), delay);
                    return Mono.delay(delay).flatMapMany(tick -> Flux.from(stage));
                })
                .switchIfEmpty(stage);
    }
}
