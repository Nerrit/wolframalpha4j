package it.nerr.wolframalpha4j.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {

    private final Map<String, Object> fields = new HashMap<>();

    public Map<String, Object> getFields() {
        return fields;
    }

    @JsonAnySetter
    public void anySetter(String key, Object value) {
        fields.put(key, value);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" + "fields=" + fields + '}';
    }
}
