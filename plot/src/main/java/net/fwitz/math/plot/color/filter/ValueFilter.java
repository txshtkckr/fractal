package net.fwitz.math.plot.color.filter;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class ValueFilter<V> implements Function<V, V> {
    @SuppressWarnings("unchecked")
    private static final ValueFilter IDENTITY = new ValueFilter("Identity", Function.identity());

    private final String name;
    private final Function<V, V> delegate;

    public ValueFilter(String name, Function<V, V> delegate) {
        this.name = requireNonNull(name, "name");
        this.delegate = requireNonNull(delegate, "delegate");
    }

    public String name() {
        return name;
    }

    public V apply(V value) {
        return delegate.apply(value);
    }

    @SuppressWarnings("unchecked")
    public static <V> ValueFilter<V> identity() {
        return IDENTITY;
    }

    @Override
    public String toString() {
        return "ValueFilter[name=" + name + ']';
    }
}
