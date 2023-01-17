package it.nerr.wolframalpha4j.types;

public enum Units {

    METRIC("metric"),
    IMPERIAL("imperial");

    private final String value;

    Units(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
