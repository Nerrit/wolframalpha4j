package it.nerr.wolframalpha4j.request;

import io.netty.handler.codec.http.HttpHeaders;
import it.nerr.wolframalpha4j.http.client.ClientException;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;
import reactor.util.Loggers;
import reactor.util.context.Context;

import java.time.Duration;

public class RateLimitRetryOperator {

    private static final Logger LOGGER = Loggers.getLogger(RateLimitRetryOperator.class);

    private final Scheduler backoffScheduler;

    public RateLimitRetryOperator(Scheduler backoffScheduler) {
        this.backoffScheduler = backoffScheduler;
    }

    public Publisher<Context> apply(Flux<Throwable> errors) {
        return errors.index().concatMap(tuple -> retry(tuple.getT2(), tuple.getT1() + 1L));
    }

    private Publisher<Context> retry(Throwable error, long iteration) {
        if (!isRateLimitError(error)) {
            return Mono.error(error);
        } else {
            ClientException clientException = (ClientException) error;
            HttpHeaders headers = clientException.getHeaders();
            try {
                boolean global = Boolean.parseBoolean(headers.get("X-RateLimit-Global"));
                Context context = Context.of("iteration", iteration);
                String retryAfter = headers.get("Retry-After");
                String resetAfter = headers.get("X-RateLimit-Reset-After");
                if (global) {
                    Duration fixedBackoff = Duration.ofSeconds(Long.parseLong(retryAfter));
                    return retryMono(fixedBackoff).thenReturn(context);
                } else if (resetAfter != null) {
                    long resetAt = (long) (Double.parseDouble(resetAfter) * 1000);
                    Duration fixedBackoff = Duration.ofMillis(resetAt);
                    return retryMono(fixedBackoff).thenReturn(context);
                } else {
                    Duration fixedBackoff = Duration.ofSeconds(Long.parseLong(retryAfter));
                    return retryMono(fixedBackoff).thenReturn(context);
                }
            } catch (Exception e) {
                LOGGER.error("Unable to parse rate limit headers: {}", headers);
                return Mono.error(e);
            }
        }
    }

    private boolean isRateLimitError(Throwable error) {
        if (error instanceof ClientException) {
            ClientException clientException = (ClientException) error;
            return clientException.getStatus().code() == 429;
        }
        return false;
    }

    private Mono<Long> retryMono(Duration delay) {
        if (delay == Duration.ZERO) {
            return Mono.just(0L);
        } else {
            return Mono.delay(delay, backoffScheduler);
        }
    }
}
