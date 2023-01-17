package it.nerr.wolframalpha4j.service;

import it.nerr.wolframalpha4j.route.Router;
import it.nerr.wolframalpha4j.types.Color;
import it.nerr.wolframalpha4j.types.Layout;
import it.nerr.wolframalpha4j.types.Units;
import it.nerr.wolframalpha4j.route.Routes;
import reactor.core.publisher.Mono;

import java.io.InputStream;

public class SimpleService extends RestService {

    public SimpleService(Router router) {
        super(router);
    }

    public Mono<InputStream> simpleQuery(String input, Layout layout, Color background, Color foreground, int fontsize,
                                         int width, Units units, int timeout) {
        return Routes.SIMPLE.newRequest()
                .query("appid", System.getenv("WOLFRAM_APP_ID"))
                .query("i", input)
                .query("layout", layout.toString())
                .query("background", background.toString())
                .query("foreground", foreground.toString())
                .query("fontsize", fontsize)
                .query("width", width)
                .query("units", units.getValue())
                .query("timeout", timeout)
                .exchange(getRouter())
                .bodyToMono(InputStream.class);
    }

    public Mono<InputStream> simpleQuery(String input, Layout layout, Color background, Color foreground, int fontsize,
                                   int width, Units units) {
        return Routes.SIMPLE.newRequest()
                .query("appid", System.getenv("WOLFRAM_APP_ID"))
                .query("i", input)
                .query("layout", layout.toString())
                .query("background", background.toString())
                .query("foreground", foreground.toString())
                .query("fontsize", fontsize)
                .query("width", width)
                .query("units", units.getValue())
                .exchange(getRouter())
                .bodyToMono(InputStream.class);
    }

    public Mono<InputStream> simpleQuery(String input, Units units, int timeout) {
        return Routes.SIMPLE.newRequest()
                .query("appid", System.getenv("WOLFRAM_APP_ID"))
                .query("i", input)
                .query("units", units.getValue())
                .query("timeout", timeout)
                .exchange(getRouter())
                .bodyToMono(InputStream.class);
    }

    public Mono<InputStream> simpleQuery(String input, Units units) {
        return Routes.SIMPLE.newRequest()
                .query("appid", System.getenv("WOLFRAM_APP_ID"))
                .query("i", input)
                .query("units", units.getValue())
                .exchange(getRouter())
                .bodyToMono(InputStream.class);
    }
}
