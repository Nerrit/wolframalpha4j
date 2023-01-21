package it.nerr.wolframalpha4j.types;

public record Timeouts(double scanTimeout, double podTimeout, double formatTimeout, double parseTimeout,
                       double totalTimeout, String async) {

    public Timeouts(double scanTimeout, double podTimeout, double formatTimeout, double parseTimeout,
                    double totalTimeout, double async) {
        this(scanTimeout, podTimeout, formatTimeout, parseTimeout, totalTimeout, String.valueOf(async));
    }

    public Timeouts(double scanTimeout, double podTimeout, double formatTimeout, double parseTimeout,
                    double totalTimeout, boolean async) {
        this(scanTimeout, podTimeout, formatTimeout, parseTimeout, totalTimeout, String.valueOf(async));
    }

    public static Timeouts defaultTimeouts() {
        return new Timeouts(3.0, 4.0, 8.0, 5.0, 20.0, "false");
    }
}
