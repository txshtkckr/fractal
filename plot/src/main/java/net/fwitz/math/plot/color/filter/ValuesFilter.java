package net.fwitz.math.plot.color.filter;

import static java.util.Objects.requireNonNull;

public class ValuesFilter<V> implements ValuesFilterFunction<V> {
    @SuppressWarnings("unchecked")
    private static final ValuesFilter IDENTITY = new ValuesFilter("Identity", (r, i, v) -> v);

    private final String name;
    private final ValuesFilterFunction<V> delegate;

    public ValuesFilter(String name, ValuesFilterFunction<V> delegate) {
        this.name = requireNonNull(name, "name");
        this.delegate = requireNonNull(delegate, "delegate");
    }

    public String name() {
        return name;
    }

    @Override
    public V[] filter(double[] reValues, double imValue, V[] rowValues) {
        return delegate.filter(reValues, imValue, rowValues);
    }

    @SuppressWarnings("unchecked")
    public static <V> ValuesFilter<V> identity() {
        return IDENTITY;
    }

    @Override
    public String toString() {
        return "ValuesFilter[name=" + name + ']';
    }
}
