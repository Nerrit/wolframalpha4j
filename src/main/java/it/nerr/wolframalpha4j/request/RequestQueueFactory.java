package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.sinks.EmissionStrategy;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.util.function.Function;

public interface RequestQueueFactory {

    <T> RequestQueue<T> create();

    static RequestQueueFactory createFromSink(Function<Sinks.ManySpec, Sinks.Many<Object>> requestSinkFactory,
                                              EmissionStrategy emissionStrategy) {
        return new SinksRequestQueueFactory(requestSinkFactory, emissionStrategy);
    }

    static RequestQueueFactory buffering() {
        return RequestQueueFactory.createFromSink(
                spec -> spec.multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false),
                EmissionStrategy.park(Duration.ofMillis(10)));
    }
}
