package it.nerr.wolframalpha4j.http;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public interface ExchangeStrategies {

    static ExchangeStrategies jackson(ObjectMapper mapper) {
        List<WriterStrategy<?>> writerStrategies = new ArrayList<>();
        writerStrategies.add(new MultipartWriterStrategy(mapper));
        writerStrategies.add(new JacksonWriterStrategy(mapper));
        writerStrategies.add(new EmptyWriterStrategy());
        List<ReaderStrategy<?>> readerStrategies = new ArrayList<>();
        readerStrategies.add(new ImageReaderStrategy());
        readerStrategies.add(new JacksonReaderStrategy<>(mapper));
        readerStrategies.add(new EmptyReaderStrategy());
        readerStrategies.add(new FallbackReaderStrategy());
        return new DefaultExchangeStrategies(writerStrategies, readerStrategies);
    }

    List<WriterStrategy<?>> writers();

    List<ReaderStrategy<?>> readers();

    class Builder {

        private final List<WriterStrategy<?>> writerStrategies = new ArrayList<>();
        private final List<ReaderStrategy<?>> readerStrategies = new ArrayList<>();

        public Builder writerStrategy(WriterStrategy<?> writerStrategy) {
            writerStrategies.add(writerStrategy);
            return this;
        }

        public Builder readerStrategy(ReaderStrategy<?> readerStrategy) {
            readerStrategies.add(readerStrategy);
            return this;
        }

        public ExchangeStrategies build() {
            return new DefaultExchangeStrategies(writerStrategies, readerStrategies);
        }
    }

}
