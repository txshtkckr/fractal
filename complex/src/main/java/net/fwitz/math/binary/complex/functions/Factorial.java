package net.fwitz.math.binary.complex.functions;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.binary.complex.analysis.Gamma;

public class Factorial {
    private static final long[] FACTORIAL = {
            1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600,
            6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L,
            121645100408832000L, 2432902008176640000L
    };
    public static final int MAX_INT_VALUE = FACTORIAL.length - 1;

    public static long factorial(int n) {
        try {
            return FACTORIAL[n];
        } catch (ArrayIndexOutOfBoundsException e) {
            // Larger values would overflow Long.MAX_VALUE
            throw new IllegalArgumentException("n must be non-negative and less than " + FACTORIAL.length + ": " + n);
        }
    }

    public static double factorial(double n) {
        return Gamma.gamma(n + 1);
    }

    public static Complex factorial(Complex z) {
        return Gamma.gamma(z.plus(1));
    }

    public static double binom(double n, double k) {
        double nf = factorial(n);
        double nmkf = factorial(n - k);
        double kf = factorial(k);
        return nf / (nmkf * kf);
    }

    public static Complex binom(Complex n, Complex k) {
        return factorial(n)
                .div(factorial(n.minus(k)))
                .div(factorial(k));
    }

}
