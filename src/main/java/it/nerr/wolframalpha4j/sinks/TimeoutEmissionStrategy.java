package it.nerr.wolframalpha4j.sinks;

import reactor.core.publisher.Sinks;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

public class TimeoutEmissionStrategy implements EmissionStrategy {

    private static final Logger LOGGER = Loggers.getLogger(TimeoutEmissionStrategy.class);

    private final long parkNanos;
    private final long timeoutNanos;
    private final boolean errorOnTimeout;

    TimeoutEmissionStrategy(long parkNanos, long timeoutNanos, boolean errorOnTimeout) {
        this.parkNanos = parkNanos;
        this.timeoutNanos = timeoutNanos;
        this.errorOnTimeout = errorOnTimeout;
    }

    @Override
    public <T> boolean emitNext(Sinks.Many<T> sink, T element) {
        long remaining = 0;
        if (timeoutNanos > 0) {
            remaining = timeoutNanos;
        }
        for (;;) {
            Sinks.EmitResult emission = sink.tryEmitNext(element);
            if (emission.isSuccess()) {
                return true;
            }
            remaining -= parkNanos;
            if (timeoutNanos >= 0 && remaining <= 0) {
                LOGGER.debug("Emission timed out at {}: {}", sink.name(), element.toString());
                if (errorOnTimeout) {
                    throw new Sinks.EmissionException(emission, "Emission timed out");
                }
                return false;
            }
            switch (emission) {
                case FAIL_ZERO_SUBSCRIBER:
                case FAIL_CANCELLED:
                case FAIL_TERMINATED:
                    return false;
                case FAIL_NON_SERIALIZED:
                    LockSupport.parkNanos(parkNanos);
                    continue;
                case FAIL_OVERFLOW:
                    LOGGER.trace("Emission overflowing at {}: {}", sink.name(), element.toString());
                    LockSupport.parkNanos(parkNanos);
                    continue;
                default:
                    throw new Sinks.EmissionException(emission, "Unknown emitResult value");
            }
        }
    }

    @Override
    public <T> boolean emitComplete(Sinks.Many<T> sink) {
        return emitTerminal(sink::tryEmitComplete);
    }

    @Override
    public <T> boolean emitError(Sinks.Many<T> sink, Throwable error) {
        return emitTerminal(() -> sink.tryEmitError(error));
    }

    private <T> boolean emitTerminal(Supplier<Sinks.EmitResult> resultSupplier) {
        long remaining = 0;
        if (timeoutNanos > 0) {
            remaining = timeoutNanos;
        }
        for (;;) {
            Sinks.EmitResult emission = resultSupplier.get();
            if (emission.isSuccess()) {
                return true;
            }
            remaining -= parkNanos;
            if (timeoutNanos >= 0 && remaining <= 0) {
                if (errorOnTimeout) {
                    throw new Sinks.EmissionException(emission, "Emission timed out");
                }
                return false;
            }
            switch (emission) {
                case FAIL_ZERO_SUBSCRIBER:
                case FAIL_CANCELLED:
                case FAIL_TERMINATED:
                case FAIL_OVERFLOW:
                    return false;
                case FAIL_NON_SERIALIZED:
                    LockSupport.parkNanos(parkNanos);
                    continue;
                default:
                    throw new Sinks.EmissionException(emission, "Unknown emitResult value");
            }
        }
    }
}
