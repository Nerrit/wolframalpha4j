package it.nerr.wolframalpha4j.request;

import reactor.core.publisher.Flux;

public interface RequestQueue<T> {

    boolean push(T request);

    Flux<T> requests();
}
