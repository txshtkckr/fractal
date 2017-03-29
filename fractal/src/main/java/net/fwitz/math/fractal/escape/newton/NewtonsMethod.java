package net.fwitz.math.fractal.escape.newton;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static net.fwitz.math.complex.Complex.complex;
import static net.fwitz.math.complex.calculus.Derivation.derivative;
import static net.fwitz.math.fractal.escape.EscapeTimeResult.contained;
import static net.fwitz.math.fractal.escape.EscapeTimeResult.escaped;

public class NewtonsMethod {
    private static final int DEFAULT_MAX_ITERS = 100;
    private static final double EPSILON = 1e-14;
    private static final double TOLERANCE = 1e-7;

    public static EscapeFunction newtonsMethod(Function<Complex, Complex> f) {
        final Function<Complex, Complex> df = derivative(f);
        return newtonsMethod(f, df, DEFAULT_MAX_ITERS);
    }

    public static EscapeFunction newtonsMethod(Function<Complex, Complex> f, int maxIters) {
        final Function<Complex, Complex> df = derivative(f);
        return newtonsMethod(f, df, maxIters);
    }

    public static EscapeFunction newtonsMethod(Function<Complex, Complex> f, Function<Complex, Complex> df) {
        return newtonsMethod(f, df, DEFAULT_MAX_ITERS);
    }

    public static EscapeFunction newtonsMethod(Function<Complex, Complex> f, Function<Complex, Complex> df, int maxIters) {
        requireNonNull(f, "f");
        requireNonNull(df, "df");
        if (maxIters < 1) {
            throw new IllegalArgumentException("maxIters < 1: " + maxIters);
        }

        return c -> {
            Complex z = c;
            for (int i = 1; i < maxIters; ++i) {
                if (!Double.isFinite(z.x()) || !Double.isFinite(z.y())) {
                    return contained(maxIters, z);
                }

                Complex dfz = df.apply(z);
                if (dfz.abs() < EPSILON) {
                    // Assume non-convergence rather than divide by such a tiny value
                    return contained(maxIters, z);
                }

                Complex fz = f.apply(z);
                Complex zPrev = z;
                z = z.minus(fz.div(dfz));
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
        // Supply the actual derivative
        final EscapeFunction ef1 = newtonsMethod(z -> z.pow3().minus(1), z -> z.pow2().times(3));
        // Use a numerical approximation for the derivative
        final EscapeFunction ef2 = newtonsMethod(z -> z.pow3().minus(1));

        final Complex c = complex(re, im);
        System.out.println("c=" + c + " [1] => " + ef1.apply(c));
        System.out.println("c=" + c + " [2] => " + ef2.apply(c));
    }
}
