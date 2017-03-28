package net.fwitz.math.plot.complex.filter;

public interface ValuesFilterFunction<V> {
    V[] filter(double[] reValues, double imValue, V[] rowValues);
}
