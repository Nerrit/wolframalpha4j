package it.nerr.wolframalpha4j.sinks;

import reactor.core.publisher.Sinks;

import java.time.Duration;

public interface EmissionStrategy {

    static TimeoutEmissionStrategy timeoutDrop(Duration duration) {
        return new TimeoutEmissionStrategy(Duration.ofMillis(10).toNanos(), duration.toNanos(), false);
    }

    static TimeoutEmissionStrategy timeoutError(Duration duration) {
        return new TimeoutEmissionStrategy(Duration.ofMillis(10).toNanos(), duration.toNanos(), true);
    }

    static EmissionStrategy park(Duration duration) {
        return new ParkEmissionStrategy(duration.toNanos());
    }

    <T> boolean emitNext(Sinks.Many<T> sink, T element);

    <T> boolean emitComplete(Sinks.Many<T> sink);

    <T> boolean emitError(Sinks.Many<T> sink, Throwable error);
}
