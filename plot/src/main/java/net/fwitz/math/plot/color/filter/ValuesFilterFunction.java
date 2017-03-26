package net.fwitz.math.plot.color.filter;

public interface ValuesFilterFunction<V> {
    V[] filter(double[] reValues, double imValue, V[] rowValues);
}
