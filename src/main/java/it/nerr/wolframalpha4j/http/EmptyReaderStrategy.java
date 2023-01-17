package it.nerr.wolframalpha4j.http;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

public class EmptyReaderStrategy implements ReaderStrategy<Void> {

    @Override
    public boolean canRead(@Nullable Class<?> type, @Nullable String contentType) {
        return type == Void.class;
    }

    @Override
    public Mono<Void> read(Mono<ByteBuf> content, Class<Void> responseType) {
        return Mono.empty();
    }
}
