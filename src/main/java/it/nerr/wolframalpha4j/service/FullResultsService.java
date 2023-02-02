package it.nerr.wolframalpha4j.service;

import it.nerr.simpleapi.route.Router;
import it.nerr.simpleapi.service.RestService;
import it.nerr.wolframalpha4j.route.Routes;
import it.nerr.wolframalpha4j.types.*;
import reactor.core.publisher.Mono;

import java.util.List;

public class FullResultsService extends RestService {

    private final String WOLFRAM_APP_ID = System.getenv("WOLFRAM_APP_ID");
    private final String APP_ID = "appid";
    private final String INPUT = "input";
    private final String OUTPUT = "output";
    private final String FORMAT = "format";
    private final String WIDTH = "width";
    private final String MAX_WIDTH = "maxwidth";
    private final String PLOT_WIDTH = "plotwidth";
    private final String MAG = "mag";
    private final String SCAN_TIMEOUT = "scantimeout";
    private final String POD_TIMEOUT = "podtimeout";
    private final String FORMAT_TIMEOUT = "formattimeout";
    private final String PARSE_TIMEOUT = "parsetimeout";
    private final String TOTAL_TIMEOUT = "totaltimeout";
    private final String ASYNC = "async";

    public FullResultsService(Router router) {
        super(router);
    }

    private record Result(QueryResult queryresult) {}

    public Mono<QueryResult> query(String input) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Format format) {
        return query(input, format.toString());
    }

    public Mono<QueryResult> query(String input, List<Format> formats) {
        return query(input, formatList(formats));
    }

    private Mono<QueryResult> query(String input, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Format format) {
        return query(input, location, format.toString());
    }

    public Mono<QueryResult> query(String input, Location location, List<Format> formats) {
        return query(input, location, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Location location, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Size size) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Size size, Format format) {
        return query(input, size, format.toString());
    }

    public Mono<QueryResult> query(String input, Size size, List<Format> formats) {
        StringBuilder sb = new StringBuilder();
        sb.append(formats.get(0).toString());
        for (int i = 1; i < formats.size(); i++) {
            sb.append(",").append(formats.get(i).toString());
        }
        return query(input, size, sb.toString());
    }

    private Mono<QueryResult> query(String input, Size size, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Size size, Location location) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(location.getParameterName(), location.getValue())
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Size size, Location location, Format format) {
        return query(input, size, location, format.toString());
    }

    public Mono<QueryResult> query(String input, Size size, Location location, List<Format> formats) {
        return query(input, size, location, formatList(formats));
    }

    public Mono<QueryResult> query(String input, Size size, Location location, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(location.getParameterName(), location.getValue())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Format format) {
        return query(input, timeouts, format.toString());
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, List<Format> formats) {
        return query(input, timeouts, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Timeouts timeouts, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Location location) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(location.getParameterName(), location.getValue())
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Location location, Format format) {
        return query(input, timeouts, location, format.toString());
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Location location, List<Format> formats) {
        return query(input, timeouts, location, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Timeouts timeouts, Location location, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(location.getParameterName(), location.getValue())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Size size) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Size size, Format format) {
        return query(input, timeouts, size, format.toString());
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Size size, List<Format> formats) {
        return query(input, timeouts, size, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Timeouts timeouts, Size size, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Location location, Size size) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(location.getParameterName(), location.getValue())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Location location, Size size, Format format) {
        return query(input, timeouts, location, size, format.toString());
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Location location, Size size, List<Format> formats) {
        return query(input, timeouts, location, size, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Timeouts timeouts, Location location, Size size, String format) {
        return Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(location.getParameterName(), location.getValue())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString())
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Miscellaneous misc) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Miscellaneous misc, Format format) {
        return query(input, misc, format.toString());
    }

    public Mono<QueryResult> query(String input, Miscellaneous misc, List<Format> formats) {
        return query(input, misc, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Miscellaneous misc, String format) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Miscellaneous misc) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Miscellaneous misc, Format format) {
        return query(input, timeouts, misc, format.toString());
    }

    public Mono<QueryResult> query(String input, Timeouts timeouts, Miscellaneous misc, List<Format> formats) {
        return query(input, timeouts, misc, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Timeouts timeouts, Miscellaneous misc, String format) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Miscellaneous misc) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Miscellaneous misc, Format format) {
        return query(input, location, misc, format.toString());
    }

    public Mono<QueryResult> query(String input, Location location, Miscellaneous misc, List<Format> formats) {
        return query(input, location, misc, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Location location, Miscellaneous misc, String format) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Size size, Miscellaneous misc) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Size size, Miscellaneous misc, Format format) {
        return query(input, size, misc, format.toString());
    }

    public Mono<QueryResult> query(String input, Size size, Miscellaneous misc, List<Format> formats) {
        return query(input, size, misc, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Size size, Miscellaneous misc, String format) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Miscellaneous misc) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Miscellaneous misc, Format format) {
        return query(input, location, timeouts, misc, format.toString());
    }

    public Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Miscellaneous misc, List<Format> formats) {
        return query(input, location, timeouts, misc, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Miscellaneous misc, String format) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Size size, Miscellaneous misc) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Size size, Miscellaneous misc, Format format) {
        return query(input, location, size, misc, format.toString());
    }

    public Mono<QueryResult> query(String input, Location location, Size size, Miscellaneous misc, List<Format> formats) {
        return query(input, location, size, misc, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Location location, Size size, Miscellaneous misc, String format) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Size size, Miscellaneous misc) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    public Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Size size, Miscellaneous misc, Format format) {
        return query(input, location, timeouts, size, misc, format.toString());
    }

    public Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Size size, Miscellaneous misc, List<Format> formats) {
        return query(input, location, timeouts, size, misc, formatList(formats));
    }

    private Mono<QueryResult> query(String input, Location location, Timeouts timeouts, Size size, Miscellaneous misc, String format) {
        return misc.addParameters(Routes.FULL_RESULT.newRequest()
                .query(APP_ID, WOLFRAM_APP_ID)
                .query(INPUT, input)
                .query(location.getParameterName(), location.getValue())
                .query(SCAN_TIMEOUT, timeouts.scanTimeout())
                .query(POD_TIMEOUT, timeouts.podTimeout())
                .query(FORMAT_TIMEOUT, timeouts.formatTimeout())
                .query(PARSE_TIMEOUT, timeouts.parseTimeout())
                .query(TOTAL_TIMEOUT, timeouts.totalTimeout())
                .query(ASYNC, timeouts.async())
                .query(WIDTH, size.width())
                .query(MAX_WIDTH, size.maxWidth())
                .query(PLOT_WIDTH, size.plotWidth())
                .query(MAG, size.mag())
                .query(FORMAT, format)
                .query(OUTPUT, Format.JSON.toString()))
                .exchange(getRouter())
                .bodyToMono(Result.class).map(Result::queryresult);
    }

    private <T> String formatList(List<T> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(list.get(0).toString());
        for (int i = 1; i < list.size(); i++) {
            sb.append(",").append(list.get(i).toString());
        }
        return sb.toString();
    }
}
