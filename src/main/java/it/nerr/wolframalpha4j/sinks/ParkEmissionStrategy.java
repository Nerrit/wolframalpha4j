package it.nerr.wolframalpha4j.sinks;

import reactor.core.publisher.Sinks;

import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

public class ParkEmissionStrategy implements EmissionStrategy {

    private final long parkNanos;

    ParkEmissionStrategy(long parkNanos) {
        this.parkNanos = parkNanos;
    }

    @Override
    public <T> boolean emitNext(Sinks.Many<T> sink, T element) {
        for (;;) {
            Sinks.EmitResult emission = sink.tryEmitNext(element);
            if (emission.isSuccess()) {
                return true;
            }
            switch (emission) {
                case FAIL_ZERO_SUBSCRIBER:
                case FAIL_CANCELLED:
                case FAIL_TERMINATED:
                    return false;
                case FAIL_NON_SERIALIZED:
                    continue;
                case FAIL_OVERFLOW:
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
        for (;;) {
            Sinks.EmitResult emission = resultSupplier.get();
            if (emission.isSuccess()) {
                return true;
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
