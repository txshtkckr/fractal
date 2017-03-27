package net.fwitz.math.complex.calculus;

import net.fwitz.math.complex.Complex;

import java.util.function.DoubleFunction;
import java.util.function.Function;

public class Derivation {
    private static final double EPSILON = 1e-7;
    private static final double ONE_OVER_TWO_EPSILON = 0.5 / EPSILON;

    // Poor man's approximate derivative by taking the slope over a small segment.
    public static DoubleFunction<Double> derivative(DoubleFunction<Double> f) {
        return x -> {
            double fxph = f.apply(x + EPSILON);
            double fxmh = f.apply(x - EPSILON);
            return (fxph - fxmh) * ONE_OVER_TWO_EPSILON;
        };
    }

    // Poor man's approximate derivative by taking the slope over a small segment.
    // Provided the derivative exists, it does not matter which direction we take it in,
    // so we'll just vary the real component for simplicity.
    public static Function<Complex, Complex> derivative(Function<Complex, Complex> f) {
        return z -> {
            Complex fzph = f.apply(z.plus(EPSILON));
            Complex fzmh = f.apply(z.minus(EPSILON));
            return fzph.minus(fzmh).times(ONE_OVER_TWO_EPSILON);
        };
    }
}
