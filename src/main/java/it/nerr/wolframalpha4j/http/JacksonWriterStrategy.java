package it.nerr.wolframalpha4j.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.netty.buffer.ByteBufAllocator;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.annotation.Nullable;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JacksonWriterStrategy implements WriterStrategy<Object> {

    private static final Logger LOGGER = Loggers.getLogger(JacksonWriterStrategy.class);

    private final ObjectMapper objectMapper;

    public JacksonWriterStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean canWrite(@Nullable Class<?> type, @Nullable String contentType) {
        if (type == null || contentType == null || !contentType.startsWith("application/json")) {
            return false;
        }
        Class<?> rawClass = getJavaType(type).getRawClass();

        return (Object.class == rawClass)
                || !String.class.isAssignableFrom(rawClass) && objectMapper.canSerialize(rawClass);
    }

    @Override
    public Mono<HttpClient.ResponseReceiver<?>> write(HttpClient.RequestSender sender, @Nullable Object body) {
        if (body == null) {
            return Mono.error(new RuntimeException("Missing body"));
        }
        Mono<String> source = Mono.fromCallable(() -> mapToString(body))
                .doOnNext(json -> {
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("{}", json);
                    }
                });
        return Mono.fromCallable(() -> sender.send(
                ByteBufFlux.fromString(source, StandardCharsets.UTF_8, ByteBufAllocator.DEFAULT)));
    }

    private String mapToString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw Exceptions.propagate(e);
        }
    }

    private JavaType getJavaType(Type type) {
        TypeFactory typeFactory = this.objectMapper.getTypeFactory();
        return typeFactory.constructType(type);
    }
}
