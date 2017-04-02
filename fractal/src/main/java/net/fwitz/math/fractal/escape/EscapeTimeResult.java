package net.fwitz.math.fractal.escape;

import net.fwitz.math.binary.complex.Complex;

import java.util.OptionalDouble;

import static java.util.Objects.requireNonNull;

public class EscapeTimeResult {
    private final boolean escaped;
    private final int iters;
    private final Complex z;
    private final OptionalDouble smoothing;

    private EscapeTimeResult(boolean escaped, int iters, Complex z, OptionalDouble smoothing) {
        this.escaped = escaped;
        this.iters = iters;
        this.z = requireNonNull(z, "z");
        this.smoothing = requireNonNull(smoothing, "smoothing");
    }

    public static EscapeTimeResult contained(Complex z, OptionalDouble smoothing) {
        return new EscapeTimeResult(false, 0, z, smoothing);
    }

    public static EscapeTimeResult escaped(int iters, Complex z, OptionalDouble smoothing) {
        return new EscapeTimeResult(true, iters, z, smoothing);
    }

    public int iters() {
        return iters;
    }

    public Complex z() {
        return z;
    }

    public boolean escaped() {
        return escaped;
    }

    public boolean contained() {
        return !escaped;
    }

    public OptionalDouble smoothing() {
        return smoothing;
    }

    @Override
    public String toString() {
        return "EscapeFunction.Result[escaped=" + escaped +
                "; iters=" + iters +
                "; z=" + z +
                "; smoothing=" + smoothing +
                ']';
    }
}
