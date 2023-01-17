package it.nerr.wolframalpha4j.http.client;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.ReferenceCounted;
import it.nerr.wolframalpha4j.http.ExchangeStrategies;
import it.nerr.wolframalpha4j.http.ReaderStrategy;
import it.nerr.wolframalpha4j.request.WebRequest;
import it.nerr.wolframalpha4j.response.ErrorResponse;
import it.nerr.wolframalpha4j.response.ResponseFunction;
import reactor.core.publisher.Mono;
import reactor.netty.NettyInbound;
import reactor.netty.http.client.HttpClientResponse;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.annotation.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class ClientResponse {

    private static final Logger LOGGER = Loggers.getLogger(ClientResponse.class);

    private final HttpClientResponse response;
    private final NettyInbound inbound;
    private final ExchangeStrategies exchangeStrategies;
    private final ClientRequest clientRequest;
    private final List<ResponseFunction> responseFunctions;
    private final AtomicBoolean reject = new AtomicBoolean();

    public ClientResponse(HttpClientResponse response, NettyInbound inbound, ExchangeStrategies exchangeStrategies,
                          ClientRequest clientRequest, List<ResponseFunction> responseFunctions) {
        this.response = response;
        this.inbound = inbound;
        this.exchangeStrategies = exchangeStrategies;
        this.clientRequest = clientRequest;
        this.responseFunctions = responseFunctions;
    }

    private static <T> ReaderStrategy<T> cast(ReaderStrategy<?> strategy) {
        return (ReaderStrategy<T>) strategy;
    }

    private static ClientException clientException(ClientRequest request, HttpClientResponse response,
                                                   @Nullable ErrorResponse errorResponse) {
        return new ClientException(request, response, errorResponse);
    }

    private static RuntimeException noReaderException(Object body, String contentType) {
        return new RuntimeException("No strategies to read this response: " + body + " - " + contentType);
    }

    public HttpClientResponse getHttpResponse() {
        return response;
    }

    public Mono<ByteBuf> getBody() {
        return inbound.receive().aggregate().doOnSubscribe(s -> {
            if (reject.get()) {
                throw new IllegalStateException("Response body can only be consumed once");
            }
        }).doOnCancel(() -> reject.set(true)).doOnNext(buf -> buf.touch("steam4j.client.response"));
    }

    public <T> Mono<T> bodyToMono(Class<T> responseType) {
        return Mono.defer(
                () -> {
                    if (response.status().code() >= 400) {
                        return createException().flatMap(Mono::error);
                    } else {
                        return Mono.just(this);
                    }
                }).transform(getResponseTransformers(clientRequest.getWebRequest())).flatMap(res -> {
            String responseContentType = response.responseHeaders().get(HttpHeaderNames.CONTENT_TYPE);
            Optional<ReaderStrategy<?>> readerStrategy =
                    exchangeStrategies.readers().stream().filter(s -> s.canRead(responseType, responseContentType))
                            .findFirst();
            return readerStrategy.map(ClientResponse::<T>cast).map(s -> s.read(getBody(), responseType))
                    .orElseGet(() -> Mono.error(noReaderException(responseType, responseContentType)))
                    .checkpoint("Body from " + clientRequest.getDescription() + " [ClientResponse]");
        });
    }

    private Function<Mono<ClientResponse>, Mono<ClientResponse>> getResponseTransformers(WebRequest webRequest) {
        return responseFunctions.stream().map(rt -> rt.transform(webRequest).andThen(
                        mono -> mono.checkpoint("Apply " + rt + " to " + webRequest.getDescription() + " [ClientResponse]")))
                .reduce(Function::andThen).orElse(mono -> mono);
    }

    public Mono<ClientException> createException() {
        String responseContentType = response.responseHeaders().get(HttpHeaderNames.CONTENT_TYPE);
        Optional<ReaderStrategy<?>> readerStrategy =
                exchangeStrategies.readers().stream().filter(s -> s.canRead(ErrorResponse.class, responseContentType))
                        .findFirst();
        return Mono.justOrEmpty(readerStrategy).map(ClientResponse::<ErrorResponse>cast)
                .flatMap(s -> s.read(getBody(), ErrorResponse.class))
                .flatMap(s -> Mono.just(clientException(clientRequest, response, s)))
                .switchIfEmpty(Mono.just(clientException(clientRequest, response, null)))
                .checkpoint(response.status() + " from " + clientRequest.getDescription() + " [ClientResponse]");
    }

    public Mono<Void> skipBody() {
        return getBody().map(ReferenceCounted::release).then();
    }

    @Override
    public String toString() {
        return "ClientResponse{" + "response=" + response + '}';
    }
}
