package it.nerr.wolframalpha4j.possible;

import reactor.util.annotation.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Possible<T> {

    private static final Object ABSENT_VALUE = new Object();
    private static final Possible<?> ABSENT = new Possible<>(ABSENT_VALUE);

    /**
     * Returns a {@code Possible} with the given value.
     *
     * @param value The value the returned Possible will store.
     * @param <T> The type the value.
     * @return A {@code Possible} with the given value.
     */
    public static <T> Possible<T> of(T value) {
        return new Possible<>(Objects.requireNonNull(value));
    }

    /**
     * Returns a {@code Possible} with no value.
     *
     * @param <T> The type of the non-existent value.
     * @return An absent {@code Possible}.
     */
    @SuppressWarnings("unchecked")
    public static <T> Possible<T> absent() {
        return (Possible<T>) ABSENT;
    }

    /**
     * Flattens a {@code Possible<Optional<T>>} into a {@code Optional<T>}. The returned {@code Optional} contains a
     * value iff the given {@code Possible} is not absent and its stored {@code Optional} is present.
     *
     * @param possible The {@code Possible} to flatten.
     * @param <T> The type of the inner value.
     * @return An {@code Optional} containing if the inner value, if any.
     */
    public static <T> Optional<T> flatOpt(Possible<Optional<T>> possible) {
        return possible.toOptional().flatMap(Function.identity());
    }

    private final T value;

    private Possible(T value) {
        this.value = value;
    }

    /**
     * If no value is stored, returns true, otherwise false.
     *
     * @return True if no value is stored, otherwise false.
     */
    public boolean isAbsent() {
        return value == ABSENT_VALUE;
    }

    /**
     * Gets the stored value, if any.
     * @throws NoSuchElementException If no value is stored.
     * @return The stored value, if any.
     */
    public T get() {
        if (isAbsent()) {
            throw new NoSuchElementException();
        }
        return value;
    }

    /**
     * Converts this {@code Possible} to an {@code Optional}. The returned {@code Optional} is present iff this
     * {@code Possible} is not absent.
     *
     * @return An {@code Optional} containing this {@code Possible}'s value, if it has one.
     */
    public Optional<T> toOptional() {
        if (isAbsent()) {
            return Optional.empty();
        }
        return Optional.of(value);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Possible<?> possible = (Possible<?>) o;
        return Objects.equals(value, possible.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (isAbsent()) {
            return "Possible.absent";
        }
        return "Possible{" + value + '}';
    }
}
