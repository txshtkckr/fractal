package net.fwitz.math.fractal.escape.newton;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.fractal.escape.EscapeTimeResult;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static net.fwitz.math.complex.Complex.complex;
import static net.fwitz.math.fractal.escape.EscapeTimeResult.contained;
import static net.fwitz.math.fractal.escape.EscapeTimeResult.escaped;

public class HalleysMethod {
    private static final int DEFAULT_MAX_ITERS = 100;
    private static final double EPSILON = 1e-14;
    private static final double TOLERANCE = 1e-7;

    public static EscapeFunction halleysMethod(
            Function<Complex, Complex> f,
            Function<Complex, Complex> df,
            Function<Complex, Complex> d2f) {
        return halleysMethod(f, df, d2f, DEFAULT_MAX_ITERS);
    }

    public static EscapeFunction halleysMethod(
            Function<Complex, Complex> f,
            Function<Complex, Complex> df,
            Function<Complex, Complex> d2f,
            int maxIters) {
        requireNonNull(f, "f");
        requireNonNull(df, "df");
        requireNonNull(d2f, "d2f");
        if (maxIters < 1) {
            throw new IllegalArgumentException("maxIters < 1: " + maxIters);
        }

        return c -> {
            Complex z = c;
            for (int i = 1; i < maxIters; ++i) {
                if (!Double.isFinite(z.re()) || !Double.isFinite(z.im())) {
                    return contained(maxIters, z);
                }

                Complex fz = f.apply(z);
                Complex dfz = df.apply(z);
                Complex d2fz = d2f.apply(z);

                Complex denom = dfz.pow2().times(2).minus(fz.times(d2fz));
                if (denom.abs() < EPSILON) {
                    // Assume non-convergence rather than divide by such a tiny value
                    return contained(maxIters, z);
                }

                Complex numer = fz.times(2).times(dfz);
                Complex zPrev = z;
                z = z.minus(numer.div(denom));
                if (z.minus(zPrev).abs() <= TOLERANCE * z.abs()) {
                    return escaped(i, maxIters, z);
                }
            }

            return contained(maxIters, z);
        };
    }

    public static void main(String[] args) {
        debug(2, 0);
        debug(1.0, 0);
        debug(2.0, 0);
        debug(-1, 0.9);
        debug(-1, 0.01);
        debug(-1, 0);
        debug(-27, 0.00000001);
        debug(Double.NaN, 0);
        debug(0, Double.POSITIVE_INFINITY);
    }

    private static void debug(double re, double im) {
        final EscapeFunction ef = halleysMethod(
                z -> z.pow3().minus(1),
                z -> z.pow2().times(3),
                z -> z.times(6));
        final Complex c = complex(re, im);
        final EscapeTimeResult result = ef.apply(c);
        System.out.println("c=" + c + " => " + result);
    }
}
