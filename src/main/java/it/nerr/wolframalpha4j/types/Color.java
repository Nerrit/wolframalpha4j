package it.nerr.wolframalpha4j.types;

public class Color {

    private String color;

    public Color(String color) {
        this.color = color;
    }

    public Color(int r, int g, int b) {
        this.color = r + "," + g + "," + b;
    }

    public Color(int r, int g, int b, int a) {
        this.color = r + "," + g + "," + b + "," + a;
    }

    @Override
    public String toString() {
        return color;
    }

}
