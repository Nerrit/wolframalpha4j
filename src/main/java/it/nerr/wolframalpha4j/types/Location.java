package it.nerr.wolframalpha4j.types;

public class Location {

    private final String parameterName;
    private final String value;

    private Location(String parameterName, String value) {
        this.parameterName = parameterName;
        this.value = value;
    }

    public static Location ofIp(String ip) {
        return new Location("ip", ip);
    }

    public static Location ofLatLong(String latLong) {
        return new Location("latlong", latLong);
    }

    public static Location ofLocation(String location) {
        return new Location("location", location);
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getValue() {
        return value;
    }

}
