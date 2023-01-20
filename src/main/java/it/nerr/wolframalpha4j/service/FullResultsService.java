package it.nerr.wolframalpha4j.service;

import it.nerr.wolframalpha4j.route.Router;
import it.nerr.wolframalpha4j.route.Routes;
import it.nerr.wolframalpha4j.types.Format;
import it.nerr.wolframalpha4j.types.QueryResult;
import reactor.core.publisher.Mono;

public class FullResultsService extends RestService {

    public FullResultsService(Router router) {
        super(router);
    }

    private record Result(QueryResult queryResult) {}

    public Mono<QueryResult> query(String input) {
        return Routes.FULL_RESULT.newRequest()
                .query("appid", System.getenv("WOLFRAM_APP_ID"))
                .query("input", input)
                .query("output", Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryResult);
    }

}
