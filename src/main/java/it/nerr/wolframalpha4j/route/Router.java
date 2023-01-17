package it.nerr.wolframalpha4j.route;

import it.nerr.wolframalpha4j.request.WebRequest;
import it.nerr.wolframalpha4j.request.WebResponse;

public interface Router {

    WebResponse exchange(WebRequest request);
}
