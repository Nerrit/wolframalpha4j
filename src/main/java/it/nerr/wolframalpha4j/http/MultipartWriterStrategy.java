package it.nerr.wolframalpha4j.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.nerr.wolframalpha4j.util.MultipartRequest;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.annotation.Nullable;
import reactor.util.function.Tuple2;

import java.io.InputStream;
import java.util.List;

public class MultipartWriterStrategy implements WriterStrategy<MultipartRequest<?>> {

    private static final Logger LOGGER = Loggers.getLogger(MultipartWriterStrategy.class);

    private final ObjectMapper objectMapper;

    public MultipartWriterStrategy(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean canWrite(@Nullable Class<?> type, @Nullable String contentType) {
        return contentType != null && contentType.equals("multipart/form-data");
    }

    @Override
    public Mono<HttpClient.ResponseReceiver<?>> write(HttpClient.RequestSender send,
                                                      @Nullable MultipartRequest<?> body) {
        if (body == null) {
            return Mono.empty(); // or .error() ?
        }
        final Object createRequest = body.getJsonPayload();
        final List<Tuple2<String, InputStream>> files = body.getFiles();
        return Mono.fromCallable(() -> send.sendForm((request, form) -> {
            form.multipart(true);
            if (body.getFiles().size() == 1) {
                form.file("file", files.get(0).getT1(), files.get(0).getT2(), "application/octet-stream");
            } else {
                for (int i = 0; i < files.size(); i++) {
                    form.file("file" + i, files.get(i).getT1(), files.get(i).getT2(), "application/octet-stream");
                }
            }

            if (createRequest != null) {
                try {
                    String payload = objectMapper.writeValueAsString(createRequest);
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("{}", payload);
                    }
                    form.attr("payload_json", payload);
                } catch (JsonProcessingException e) {
                    throw Exceptions.propagate(e);
                }
            }
        }));
    }
}
