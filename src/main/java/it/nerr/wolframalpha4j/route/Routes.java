package it.nerr.wolframalpha4j.route;

import it.nerr.simpleapi.route.Route;

public abstract class Routes {

    public static final String BASE_URL = "https://api.wolframalpha.com";

    public static final String BASE_QUERY_RECOGNIZER = "https://www.wolframalpha.com/queryrecognizer";

    public static final Route SIMPLE = Route.get("/v1/simple");

    public static final Route CONVERSATION = Route.get("/v1/conversation.jsp");

    public static final Route SPOKEN = Route.get("/v1/spoken");

    public static final Route RESULT = Route.get("/v1/result");

    public static final Route FULL_RESULT = Route.get("/v2/query");

    public static final Route QUERY_RECOGNIZER = Route.get("/query.jsp");

}
