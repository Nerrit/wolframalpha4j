package it.nerr.wolframalpha4j.http;

import io.netty.buffer.ByteBuf;
import io.netty.util.IllegalReferenceCountException;
import it.nerr.wolframalpha4j.response.ErrorResponse;
import reactor.core.publisher.Mono;
import reactor.util.annotation.Nullable;

import java.nio.charset.StandardCharsets;

public class FallbackReaderStrategy implements ReaderStrategy<Object> {

    @Override
    public boolean canRead(@Nullable Class<?> type, @Nullable String contentType) {
        return true;
    }

    @Override
    public Mono<Object> read(Mono<ByteBuf> content, Class<Object> responseType) {
        if (ErrorResponse.class.isAssignableFrom(responseType)) {
            return content.handle((buf, sink) -> {
                try {
                    ErrorResponse response = new ErrorResponse();
                    response.getFields().put("body", asString(buf));
                    sink.next(response);
                } catch (IllegalReferenceCountException e) {
                    sink.complete();
                }
            });
        }
        return content.handle((buf, sink) -> {
            try {
                sink.next(asString(buf));
            } catch (IllegalReferenceCountException e) {
                sink.complete();
            }
        });
    }

    private String asString(ByteBuf buf) {
        return buf.readCharSequence(buf.readableBytes(), StandardCharsets.UTF_8).toString();
    }
}
