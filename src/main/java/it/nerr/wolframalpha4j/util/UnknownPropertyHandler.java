package it.nerr.wolframalpha4j.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

import java.io.IOException;

public class UnknownPropertyHandler extends DeserializationProblemHandler {

    private final boolean ignoreUnknown;

    public UnknownPropertyHandler(boolean ignoreUnknown) {
        this.ignoreUnknown = ignoreUnknown;
    }

    @Override
    public boolean handleUnknownProperty(DeserializationContext ctx, JsonParser parser, JsonDeserializer<?> deser,
                                         Object beanOrClass, String propertyName) throws IOException {
        if (!ignoreUnknown) {
            return false;
        }
        parser.skipChildren();
        return true;
    }
}
