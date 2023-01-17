package it.nerr.wolframalpha4j.request;

import it.nerr.wolframalpha4j.http.client.ClientResponse;
import it.nerr.wolframalpha4j.http.client.WebClient;
import it.nerr.wolframalpha4j.route.Router;
import it.nerr.wolframalpha4j.util.ReactorResources;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST;

public class DefaultRouter implements Router {

    private static final Logger LOGGER = Loggers.getLogger(DefaultRouter.class);
    private static final ResponseHeaderStrategy HEADER_STRATEGY = new ResponseHeaderStrategy();
    private static final Duration HOUSE_KEEPING_PERIOD = Duration.ofSeconds(30);

    private final ReactorResources reactorResources;
    private final WebClient httpClient;
    private final Map<BucketKey, RequestStream> streamMap = new ConcurrentHashMap<>();
    private final RouterOptions routerOptions;

    private final AtomicBoolean isHousekeeping = new AtomicBoolean(false);
    private volatile Instant lastHousekeepingTime = Instant.EPOCH;

    /**
     * Create a Discord API bucket-aware {@link Router} configured with the given options.
     *
     * @param routerOptions the options that configure this {@link Router}
     */
    public DefaultRouter(RouterOptions routerOptions) {
        this.routerOptions = routerOptions;
        this.reactorResources = routerOptions.getReactorResources();
        this.httpClient = new WebClient(reactorResources.getHttpClient(),
                routerOptions.getExchangeStrategies(), "Bot", routerOptions.getToken(),
                routerOptions.getResponseTransformers(), routerOptions.getWolframAlphaApiBaseUrl());
    }

    @Override
    public WebResponse exchange(WebRequest request) {
        return new WebResponse(Mono.deferContextual(
                        ctx -> {
                            Sinks.One<ClientResponse> callback = Sinks.one();
                            housekeepIfNecessary();
                            BucketKey bucketKey = BucketKey.of(request);
                            streamMap.compute(bucketKey, (key, value) -> {
                                RequestStream stream = value != null ? value : createStream(key, request);
                                if (!stream.push(new RequestCorrelation<>(request, callback, ctx))) {
                                    callback.emitError(new DiscardedRequestException(request), FAIL_FAST);
                                }
                                return stream;
                            });
                            return callback.asMono();
                        })
                .checkpoint("Request to " + request.getDescription() + " [DefaultRouter]"), reactorResources);
    }

    private RequestStream createStream(BucketKey bucketKey, WebRequest request) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Creating RequestStream with key {} for request: {} -> {}",
                    bucketKey, request.getRoute().getUriTemplate(), request.getCompleteUri());
        }
        RequestStream stream = new RequestStream(bucketKey, routerOptions, httpClient, HEADER_STRATEGY);
        stream.start();
        return stream;
    }

    private void housekeepIfNecessary() {
        Instant now = Instant.now();
        if (lastHousekeepingTime.plus(HOUSE_KEEPING_PERIOD).isAfter(now)) {
            return;
        }

        tryHousekeep(now);
    }

    private void tryHousekeep(Instant now) {
        if (isHousekeeping.compareAndSet(false, true)) {
            try {
                doHousekeep(now);
            } finally {
                lastHousekeepingTime = Instant.now();
                isHousekeeping.set(false);
            }
        }
    }

    private void doHousekeep(Instant now) {
        streamMap.keySet().forEach(key ->
                streamMap.compute(key, (bucketKey , stream) -> {
                    if (stream == null) {
                        return null;
                    }
                    if (stream.getResetAt().isBefore(now) && stream.countRequestsInFlight() < 1) {
                        if (LOGGER.isTraceEnabled()) {
                            LOGGER.trace("Evicting RequestStream with bucket ID {}", bucketKey);
                        }
                        stream.stop();
                        return null;
                    }
                    return stream;
                })
        );
    }
}
