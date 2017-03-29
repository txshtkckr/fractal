package net.fwitz.math.fractal.escape;

import net.fwitz.math.binary.complex.Complex;

public class EscapeTimeResult {
    private final boolean escaped;
    private final int iters;
    private final int maxIters;
    private final Complex z;

    private EscapeTimeResult(boolean escaped, int iters, int maxIters, Complex z) {
        this.escaped = escaped;
        this.iters = iters;
        this.maxIters = maxIters;
        this.z = z;
    }

    public static EscapeTimeResult contained(int maxIters, Complex z) {
        return new EscapeTimeResult(false, 0, maxIters, z);
    }

    public static EscapeTimeResult escaped(int iters, int maxIters, Complex z) {
        return new EscapeTimeResult(true, iters, maxIters, z);
    }

    public int iters() {
        return iters;
    }

    public int maxIters() {
        return maxIters;
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

    @Override
    public String toString() {
        return "EscapeFunction.Result[escaped=" + escaped +
                "; iters=" + iters +
                "; maxIters=" + maxIters +
                "; z=" + z + ']';
    }
}
