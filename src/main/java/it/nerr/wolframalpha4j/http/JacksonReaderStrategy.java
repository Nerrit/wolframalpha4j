package it.nerr.wolframalpha4j.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.util.IllegalReferenceCountException;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.annotation.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JacksonReaderStrategy<T> implements ReaderStrategy<T> {

    private static final Logger LOGGER = Loggers.getLogger(JacksonReaderStrategy.class);

    private final ObjectMapper objectMapper;

    public JacksonReaderStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static Mono<byte[]> byteArray(Mono<ByteBuf> byteBufMono) {
        return byteBufMono.handle((buf, sink) -> {
            try {
                byte[] bytes = new byte[buf.readableBytes()];
                buf.readBytes(bytes);
                sink.next(bytes);
            } catch (IllegalReferenceCountException e) {
                sink.complete();
            }
        });
    }

    @Override
    public boolean canRead(@Nullable Class<?> type, @Nullable String contentType) {
        if (type == null || contentType == null || !contentType.startsWith("application/json")) {
            return false;
        }

        // A Route<String> should be read by the FallbackReader
        return !CharSequence.class.isAssignableFrom(type) && objectMapper.canDeserialize(getJavaType(type));
    }

    private JavaType getJavaType(Type type) {
        return objectMapper.getTypeFactory().constructType(type);
    }

    @Override
    public Mono<T> read(Mono<ByteBuf> content, Class<T> responseType) {
        return content.as(JacksonReaderStrategy::byteArray)
                .map(bytes -> {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("{}", new String(bytes, StandardCharsets.UTF_8));
                    }
                    try {
                        return objectMapper.readValue(bytes, responseType);
                    } catch (JsonProcessingException e) {
                        throw Exceptions.propagate(new RuntimeException(e.toString()
                                .replaceAll("(\"token\": ?\")([A-Za-z0-9._-]*)(\")", "$1hunter2$3")));
                    } catch (IOException e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }
}
