package it.nerr.wolframalpha4j.http;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.annotation.Nullable;

public interface WriterStrategy<R> {

    boolean canWrite(@Nullable Class<?> type, @Nullable String contentType);

    Mono<HttpClient.ResponseReceiver<?>> write(HttpClient.RequestSender sender, @Nullable R body);
}
