package it.nerr.wolframalpha4j.types;

public enum Layout {

    DIVIDER("divider"),
    LABELBAR("labelbar");

    private final String value;

    Layout(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
