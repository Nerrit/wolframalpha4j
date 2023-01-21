package it.nerr.wolframalpha4j.types;

public record Size(int width, int maxWidth, int plotWidth, double mag) {

    public static Size defaultSize() {
        return new Size(500, 500, 200, 1.0);
    }
}
