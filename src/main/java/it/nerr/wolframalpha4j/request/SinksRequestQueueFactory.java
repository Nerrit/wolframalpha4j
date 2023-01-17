package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.sinks.EmissionStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.function.Function;

public class SinksRequestQueueFactory implements RequestQueueFactory {

    private static final Logger LOGGER = Loggers.getLogger(SinksRequestQueueFactory.class);

    private final Function<Sinks.ManySpec, Sinks.Many<Object>> requestSinkFactory;
    private final EmissionStrategy emissionStrategy;

    SinksRequestQueueFactory(Function<Sinks.ManySpec, Sinks.Many<Object>> requestSinkFactory,
                             EmissionStrategy emissionStrategy) {
        this.requestSinkFactory = requestSinkFactory;
        this.emissionStrategy = emissionStrategy;
    }

    @Override
    public <T> RequestQueue<T> create() {
        return new RequestQueue<T>() {

            private final Sinks.Many<Object> sink = requestSinkFactory.apply(Sinks.many());

            @Override
            public boolean push(T request) {
                return emissionStrategy.emitNext(sink, request);
            }

            @SuppressWarnings("unchecked")
            @Override
            public Flux<T> requests() {
                return (Flux<T>) sink.asFlux();
            }
        };
    }
}
