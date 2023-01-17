package it.nerr.wolframalpha4j.types;

public enum Format {

    IMAGE("image"),
    IMAGE_MAP("imagemap"),
    PLAIN_TEXT("plaintext"),
    MATH_ML("MathML"),
    SOUND("Sound"),
    WAV("wav"),
    WOLFRAM_LANGUAGE_INPUT("minput"),
    WOLFRAM_LANGUAGE_OUTPUT("moutput"),
    WOLFRAM_LANGUAGE_CELL_EXPRESSION("cell"),
    JSON("json"),
    XML("xml");

    private final String value;

    Format(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
