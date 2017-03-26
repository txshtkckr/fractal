package net.fwitz.math.complex.calculus;

import net.fwitz.math.complex.Complex;

import java.util.function.Function;

public class Derivation {
    private static final double EPSILON = 1e-7;
    private static final double EPSILON_x_2 = EPSILON * 2;

    // Poor man's approximate derivative by taking the slope over a small segment.
    // Provided the derivative exists, it does not matter which direction we take it in,
    // so we'll just vary the real component for simplicity.
    public static Function<Complex, Complex> derivative(Function<Complex, Complex> f) {
        return z -> {
            Complex fzph = f.apply(z.plus(EPSILON));
            Complex fzmh = f.apply(z.minus(EPSILON));
            return fzph.minus(fzmh).div(EPSILON_x_2);
        };
    }
}
