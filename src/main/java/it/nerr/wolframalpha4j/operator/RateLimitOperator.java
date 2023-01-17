package it.nerr.wolframalpha4j.operator;

import it.nerr.wolframalpha4j.sinks.EmissionStrategy;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class RateLimitOperator<T> implements Function<Publisher<T>, Publisher<T>> {

    private static final Logger LOGGER = Loggers.getLogger("discord4j.limiter");
    private static final Supplier<Scheduler> DEFAULT_PUBLISH_SCHEDULER = () ->
            Schedulers.newSingle("d4j-limiter", true);

    private final AtomicInteger tokens;
    private final Duration refillPeriod;
    private final Scheduler delayScheduler;
    private final Sinks.Many<Integer> tokenSink;
    private final Scheduler tokenPublishScheduler;
    private final EmissionStrategy emissionStrategy;

    public RateLimitOperator(int capacity, Duration refillPeriod, Scheduler delayScheduler) {
        this(capacity, refillPeriod, delayScheduler, DEFAULT_PUBLISH_SCHEDULER.get());
    }

    public RateLimitOperator(int capacity, Duration refillPeriod, Scheduler delayScheduler, Scheduler publishScheduler) {
        this.tokens = new AtomicInteger(capacity);
        this.refillPeriod = refillPeriod;
        this.delayScheduler = delayScheduler;
        this.tokenSink = Sinks.many().replay().latestOrDefault(capacity);
        this.tokenPublishScheduler = publishScheduler;
        this.emissionStrategy = EmissionStrategy.park(Duration.ofNanos(10));
    }

    private String id() {
        return Integer.toHexString(hashCode());
    }

    @Override
    public Publisher<T> apply(Publisher<T> source) {
        return Flux.from(source).flatMap(value -> availableTokens()
                .next()
                .doOnSubscribe(s -> {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("[{}] Subscribed to limiter", id());
                    }
                })
                .map(token -> {
                    acquire();
                    Mono.delay(refillPeriod, delayScheduler).subscribe(__ -> release());
                    return value;
                }));
    }

    private void acquire() {
        int token = tokens.decrementAndGet();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[{}] Acquired a token, {} tokens remaining", id(), token);
        }
        emissionStrategy.emitNext(tokenSink, token);
    }

    private void release() {
        int token = tokens.incrementAndGet();
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[{}] Released a token, {} tokens remaining", id(), token);
        }
        emissionStrategy.emitNext(tokenSink, token);
    }

    private Flux<Integer> availableTokens() {
        return tokenSink.asFlux().publishOn(tokenPublishScheduler).filter(__ -> tokens.get() > 0);
    }
}
