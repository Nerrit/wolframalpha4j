package it.nerr.wolframalpha4j.http;

import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.util.annotation.Nullable;

public class EmptyWriterStrategy implements WriterStrategy<Void> {

    @Override
    public boolean canWrite(@Nullable Class<?> type, @Nullable String contentType) {
        return type == null;
    }

    @Override
    public Mono<HttpClient.ResponseReceiver<?>> write(HttpClient.RequestSender sender, @Nullable Void body) {
        return Mono.just(sender.send(ByteBufFlux.empty()));
    }
}
