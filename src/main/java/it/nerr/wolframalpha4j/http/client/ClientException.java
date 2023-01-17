package it.nerr.wolframalpha4j.http.client;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import it.nerr.wolframalpha4j.response.ErrorResponse;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClientResponse;
import reactor.retry.RetryContext;
import reactor.util.annotation.Nullable;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClientException extends RuntimeException {

    private final ClientRequest request;
    private final HttpClientResponse response;
    private final ErrorResponse errorResponse;

    /**
     * Create a new {@link ClientException} with the given HTTP request and response details.
     *
     * @param request       the original {@link ClientRequest} that caused this exception
     * @param response      the failing {@link HttpClientResponse}
     * @param errorResponse the response body converted to an {@link ErrorResponse}, or {@code null} if not available
     */
    public ClientException(ClientRequest request, HttpClientResponse response, @Nullable ErrorResponse errorResponse) {
        super(request.getMethod().toString() + " " + request.getUrl() + " returned " + response.status() +
                (errorResponse != null ? " with response " + errorResponse.getFields() : ""));
        this.request = request;
        this.response = response;
        this.errorResponse = errorResponse;
    }

    /**
     * {@link Predicate} helper to further classify a {@link ClientException} depending on the underlying HTTP status
     * code.
     *
     * @param code the status code for which this {@link Predicate} should return {@code true}
     * @return a {@link Predicate} that returns {@code true} if the given {@link Throwable} is a {@link ClientException}
     * containing the given HTTP status code
     */
    public static Predicate<Throwable> isStatusCode(int code) {
        return t -> {
            if (t instanceof ClientException e) {
                return e.getStatus().code() == code;
            }
            return false;
        };
    }

    /**
     * {@link Predicate} helper to further classify a {@link ClientException} depending on the underlying HTTP status
     * code.
     *
     * @param codes the status codes for which this {@link Predicate} should return {@code true}
     * @return a {@link Predicate} that returns {@code true} if the given {@link Throwable} is a {@link ClientException}
     * containing the given HTTP status code
     */
    public static Predicate<Throwable> isStatusCode(Integer... codes) {
        return t -> {
            if (t instanceof ClientException e) {
                return Arrays.asList(codes).contains(e.getStatus().code());
            }
            return false;
        };
    }

    /**
     * {@link Predicate} helper to further classify a {@link ClientException}, while creating a {@link Retry} factory,
     * depending on the underlying HTTP status code. A {@link Retry} factory can be created through methods like
     * {@link reactor.retry.Retry#onlyIf(Predicate)} where this method can be used as argument.
     *
     * @param code the status code for which this {@link Predicate} should return {@code true}
     * @return a {@link Predicate} that returns {@code true} if the given {@link RetryContext} exception is a
     * {@link ClientException} containing the given HTTP status code
     */
    public static Predicate<RetryContext<?>> isRetryContextStatusCode(int code) {
        return ctx -> isStatusCode(code).test(ctx.exception());
    }

    /**
     * {@link Predicate} helper to further classify a {@link ClientException}, while creating a {@link Retry} factory,
     * depending on the underlying HTTP status code. A {@link Retry} factory can be created through methods like
     * {@link reactor.retry.Retry#onlyIf(Predicate)} where this method can be used as argument.
     *
     * @param codes the status codes for which this {@link Predicate} should return {@code true}
     * @return a {@link Predicate} that returns {@code true} if the given {@link Throwable} is a {@link ClientException}
     * containing the given HTTP status code
     */
    public static Predicate<RetryContext<?>> isRetryContextStatusCode(Integer... codes) {
        return ctx -> isStatusCode(codes).test(ctx.exception());
    }

    /**
     * Transformation function that can be used within an operator such as {@link Mono#transform(Function)} or
     * {@link Mono#transformDeferred(Function)} to turn an error sequence matching the given HTTP status code, into an
     * empty sequence, effectively suppressing the original error.
     *
     * @param code the status code that should be transformed into empty sequences
     * @param <T>  the type of the response
     * @return a transformation function that converts error sequences into empty sequences
     */
    public static <T> Function<Mono<T>, Publisher<T>> emptyOnStatus(int code) {
        return mono -> mono.onErrorResume(isStatusCode(code), t -> Mono.empty());
    }

    /**
     * Transformation function that can be used within an operator such as {@link Mono#transform(Function)} or
     * {@link Mono#transformDeferred(Function)} to apply a retrying strategy in case of an error matching the given HTTP
     * status code. The provided retrying strategy will wait 1 second, and then retry once.
     *
     * @param code the status code that should be retried
     * @param <T>  the type of the response
     * @return a transformation function that retries error sequences
     */
    public static <T> Function<Mono<T>, Publisher<T>> retryOnceOnStatus(int code) {
        return mono -> mono.retryWhen(Retry.backoff(1, Duration.ofSeconds(1)).filter(isStatusCode(code)));
    }

    /**
     * Return the {@link ClientRequest} encapsulating a Discord API request.
     *
     * @return the request that caused this exception
     */
    public ClientRequest getRequest() {
        return request;
    }

    /**
     * Return the {@link HttpClientResponse} encapsulating a low-level Discord API response.
     *
     * @return the low-level response that caused this exception
     */
    public HttpClientResponse getResponse() {
        return response;
    }

    /**
     * Return the {@link HttpResponseStatus} with information related to the HTTP error. The actual status code can be
     * obtained through {@link HttpResponseStatus#code()}.
     *
     * @return the HTTP error associated to this exception
     */
    public HttpResponseStatus getStatus() {
        return getResponse().status();
    }

    /**
     * Return the {@link HttpHeaders} from the error <strong>response</strong>. To get request headers refer to
     * {@link #getRequest()} and then {@link ClientRequest#getHeaders()}.
     *
     * @return the HTTP response headers
     */
    public HttpHeaders getHeaders() {
        return getResponse().responseHeaders();
    }

    /**
     * Return the HTTP response body in the form of a Discord {@link ErrorResponse}, if present. {@link ErrorResponse}
     * is a common object that contains an internal status code and messages, and could be used to further clarify the
     * source of the API error.
     *
     * @return the Discord error response, if present
     */
    public Optional<ErrorResponse> getErrorResponse() {
        return Optional.ofNullable(errorResponse);
    }
}
