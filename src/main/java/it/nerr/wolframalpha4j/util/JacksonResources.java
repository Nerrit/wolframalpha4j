package it.nerr.wolframalpha4j.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import it.nerr.wolframalpha4j.possible.PossibleFilter;
import it.nerr.wolframalpha4j.possible.PossibleModule;

import java.util.function.Function;

public class JacksonResources {

    public static final Function<ObjectMapper, ObjectMapper> INITIALIZER = mapper -> mapper
            .registerModule(new PossibleModule())
            .registerModule(new Jdk8Module())
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY)
            .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY)
            .setDefaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.CUSTOM,
                    JsonInclude.Include.ALWAYS, PossibleFilter.class, null));

    public static final Function<ObjectMapper, ObjectMapper> HANDLE_UNKNOWN_PROPERTIES =
            mapper -> mapper.addHandler(new UnknownPropertyHandler(true));

    private final ObjectMapper objectMapper;

    public JacksonResources() {
        this(HANDLE_UNKNOWN_PROPERTIES);
    }

    public JacksonResources(Function<ObjectMapper, ObjectMapper> mapper) {
        this.objectMapper = INITIALIZER.andThen(mapper).apply(new ObjectMapper());
    }

    public JacksonResources(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static JacksonResources create() {
        return new JacksonResources(HANDLE_UNKNOWN_PROPERTIES);
    }

    public static JacksonResources createFromObjectMapper(ObjectMapper objectMapper) {
        return new JacksonResources(INITIALIZER.andThen(HANDLE_UNKNOWN_PROPERTIES).apply(objectMapper));
    }

    public JacksonResources withMapperFunction(Function<ObjectMapper, ObjectMapper> transformer) {
        return new JacksonResources(transformer.apply(objectMapper));
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
