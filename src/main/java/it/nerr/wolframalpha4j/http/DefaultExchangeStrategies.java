package it.nerr.wolframalpha4j.http;

import java.util.Collections;
import java.util.List;

public class DefaultExchangeStrategies implements ExchangeStrategies {

    private final List<WriterStrategy<?>> writers;
    private final List<ReaderStrategy<?>> readers;

    public DefaultExchangeStrategies(List<WriterStrategy<?>> writers, List<ReaderStrategy<?>> readers) {
        this.writers = Collections.unmodifiableList(writers);
        this.readers = Collections.unmodifiableList(readers);
    }

    @Override
    public List<WriterStrategy<?>> writers() {
        return writers;
    }

    @Override
    public List<ReaderStrategy<?>> readers() {
        return readers;
    }
}
