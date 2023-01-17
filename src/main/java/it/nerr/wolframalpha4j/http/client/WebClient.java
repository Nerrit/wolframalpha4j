package it.nerr.wolframalpha4j.http.client;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import it.nerr.wolframalpha4j.http.ExchangeStrategies;
import it.nerr.wolframalpha4j.http.WriterStrategy;
import it.nerr.wolframalpha4j.response.ResponseFunction;
import it.nerr.wolframalpha4j.util.GitProperties;
import reactor.core.publisher.Mono;
import reactor.netty.ConnectionObserver;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientRequest;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.annotation.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Properties;

import static it.nerr.wolframalpha4j.util.LogUtil.format;

public class WebClient {

    public static final String KEY_REQUEST_TIMESTAMP = "discord4j.request.timestamp";
    private static final Logger LOGGER = Loggers.getLogger(WebClient.class);
    private final HttpClient httpClient;
    private final HttpHeaders defaultHeaders;
    private final ExchangeStrategies exchangeStrategies;
    private final List<ResponseFunction> responseFunctions;

    /**
     * Create a new {@link WebClient} wrapping HTTP, Discord and encoding/decoding resources.
     *
     * @param httpClient          a Reactor Netty HTTP client
     * @param exchangeStrategies  a strategy to transform requests and responses
     * @param authorizationScheme scheme to use with the authorization header, like "Bot" or "Bearer"
     * @param token               a Discord token for API authorization
     * @param responseFunctions   a list of {@link ResponseFunction} transformations
     */
    public WebClient(HttpClient httpClient, ExchangeStrategies exchangeStrategies,
                     String authorizationScheme, String token,
                     List<ResponseFunction> responseFunctions, String discordBaseUrl) {
        final Properties properties = GitProperties.getProperties();
        final String version = properties.getProperty(GitProperties.APPLICATION_VERSION, "3");
        final String url = properties.getProperty(GitProperties.APPLICATION_URL, "https://discord4j.com");

        final HttpHeaders defaultHeaders = new DefaultHttpHeaders();
        defaultHeaders.add(HttpHeaderNames.CONTENT_TYPE, "application/json");
        defaultHeaders.add(HttpHeaderNames.AUTHORIZATION, authorizationScheme + " " + token);
        defaultHeaders.add(HttpHeaderNames.USER_AGENT, "DiscordBot(" + url + ", " + version + ")");

        this.httpClient = configureHttpClient(httpClient.baseUrl(discordBaseUrl));
        this.defaultHeaders = defaultHeaders;
        this.exchangeStrategies = exchangeStrategies;
        this.responseFunctions = responseFunctions;
    }

    @SuppressWarnings("unchecked")
    private static <T> WriterStrategy<T> cast(WriterStrategy<?> strategy) {
        return (WriterStrategy<T>) strategy;
    }

    private static RuntimeException noWriterException(@Nullable Object body, String contentType) {
        return new RuntimeException("No strategies to write this request: " + body + " - " + contentType);
    }

    private HttpClient configureHttpClient(HttpClient httpClient) {
        if (LOGGER.isTraceEnabled()) {
            return httpClient.observe((connection, state) -> {
                if (connection instanceof ConnectionObserver) {
                    ConnectionObserver observer = (ConnectionObserver) connection;
                    LOGGER.trace(format(observer.currentContext(), "{} {}"), state, connection);
                } else if (connection instanceof HttpClientRequest) {
                    HttpClientRequest httpClientRequest = (HttpClientRequest) connection;
                    LOGGER.trace(format(httpClientRequest.currentContextView(), "{} {}"), state, connection);
                }
            });
        }
        return httpClient;
    }

    /**
     * Return the underlying Reactor Netty HTTP client.
     *
     * @return the HTTP client used by this {@link WebClient}
     */
    public HttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Return the default headers used in every request.
     *
     * @return the {@link HttpHeaders} used by this {@link WebClient} in every request
     */
    public HttpHeaders getDefaultHeaders() {
        return defaultHeaders;
    }

    /**
     * Return the strategy used for request and response conversion.
     *
     * @return the {@link ExchangeStrategies} used by this {@link WebClient} in every request
     */
    public ExchangeStrategies getExchangeStrategies() {
        return exchangeStrategies;
    }

    /**
     * Exchange a request for a {@link Mono} response.
     * <p>
     * The request will be processed according to the writer strategies available.
     *
     * @param request the client HTTP request
     * @return a {@link Mono} with the response in the form of {@link ClientResponse}
     */
    public Mono<ClientResponse> exchange(ClientRequest request) {
        return Mono.defer(
                        () -> {
                            HttpHeaders requestHeaders = buildHttpHeaders(request);
                            String contentType = requestHeaders.get(HttpHeaderNames.CONTENT_TYPE);
                            HttpClient.RequestSender sender = httpClient.headers(headers -> headers.setAll(requestHeaders))
                                    .request(request.getMethod())
                                    .uri(request.getUrl());
                            Object body = request.getBody();
                            return exchangeStrategies.writers().stream()
                                    .filter(s -> s.canWrite(body != null ? body.getClass() : null, contentType))
                                    .findFirst()
                                    .map(WebClient::cast)
                                    .map(writer -> writer.write(sender, body))
                                    .orElseGet(() -> Mono.error(noWriterException(body, contentType)));
                        })
                .flatMap(receiver -> receiver.responseConnection((response, connection) ->
                        Mono.just(new ClientResponse(response, connection.inbound(),
                                exchangeStrategies, request, responseFunctions))).next())
                .contextWrite(ctx -> ctx.put(KEY_REQUEST_TIMESTAMP, Instant.now().toEpochMilli()));
    }

    private <R> HttpHeaders buildHttpHeaders(ClientRequest request) {
        HttpHeaders headers = new DefaultHttpHeaders().add(defaultHeaders).setAll(request.getHeaders());
        if (request.getBody() == null) {
            headers.remove(HttpHeaderNames.CONTENT_TYPE);
        }
        return headers;
    }
}
