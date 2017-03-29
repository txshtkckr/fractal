package net.fwitz.math.plot.renderer.filter;

public interface ValuesFilterFunction<V> {
    V[] filter(double[] reValues, double imValue, V[] rowValues);
}
