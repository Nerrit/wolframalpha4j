package it.nerr.wolframalpha4j.http;

import io.netty.buffer.ByteBuf;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

public interface ReaderStrategy<T> {

    boolean canRead(@Nullable Class<?> type, @Nullable String contentType);

    Mono<T> read(Mono<ByteBuf> content, Class<T> responseType);
}
