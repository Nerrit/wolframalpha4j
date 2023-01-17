package it.nerr.wolframalpha4j.service;

import it.nerr.wolframalpha4j.route.Router;
import it.nerr.wolframalpha4j.route.Routes;
import it.nerr.wolframalpha4j.types.Units;
import reactor.core.publisher.Mono;

public class ShortAnswerService extends RestService {

    public ShortAnswerService(Router router) {
        super(router);
    }

    public Mono<String> shortAnswer(String input, Units units, int timeout) {
        return Routes.SPOKEN.newRequest()
                .query("appid", System.getenv("WOLFRAM_APP_ID"))
                .query("i", input)
                .query("units", units)
                .query("timeout", timeout)
                .exchange(getRouter())
                .bodyToMono(String.class);
    }

    public Mono<String> shortAnswer(String input, Units units) {
        return Routes.SPOKEN.newRequest()
                .query("appid", System.getenv("WOLFRAM_APP_ID"))
                .query("i", input)
                .query("units", units)
                .exchange(getRouter())
                .bodyToMono(String.class);
    }
}
