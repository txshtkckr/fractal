package net.fwitz.math.complex.analysis;

import net.fwitz.math.complex.Complex;

import static net.fwitz.math.complex.Complex.complex;
import static net.fwitz.math.complex.Complex.real;

public class Erf {
    private static final double TWO_OVER_SQRT_PI = 2 / Math.sqrt(Math.PI);

    public static double erf(double x) {
        if (x == 0) {
            return 0;
        }
        if (Double.isNaN(x)) {
            return Double.NaN;
        }
        if (Double.isInfinite(x)) {
            return Math.copySign(1, x);
        }

        double t = 1.0 / (1.0 + 0.5 * Math.abs(x));

        // use Horner's method
        double ans = 1 - t * Math.exp(-x * x - 1.26551223 +
                t * (1.00002368 +
                        t * (0.37409196 +
                                t * (0.09678418 +
                                        t * (-0.18628806 +
                                                t * (0.27886807 +
                                                        t * (-1.13520398 +
                                                                t * (1.48851587 +
                                                                        t * (-0.82215223 +
                                                                                t * (0.17087277))))))))));
        return Math.copySign(ans, x);
    }

    public static Complex erf(Complex z) {
        if (z.im() == 0.0) {
            return real(erf(z.re()));
        }
        Complex zSquared = z.times(z);
        Complex sum = z;
        Complex term = z;
        for (int k = 2; k < 10; ++k) {
            double scale = ((double) -2 * k + 1) / (2 * k * k + k);
            term = term.times(scale).times(zSquared);
            sum = sum.plus(term);
        }
        return sum.times(TWO_OVER_SQRT_PI);
    }

    // fractional error less than x.xx * 10 ^ -4.
    // Algorithm 26.2.17 in Abromowitz and Stegun, Handbook of Mathematical.
    public static double erf2(double x) {
        double t = 1.0 / (1.0 + 0.47047 * Math.abs(x));
        double poly = t * (0.3480242 + t * (-0.0958798 + t * (0.7478556)));
        double ans = 1.0 - poly * Math.exp(-x * x);
        return Math.copySign(ans, x);
    }

    public static double erfc(double x) {
        return 1.0 - erf(x);
    }

    public static Complex erfc(Complex z) {
        return Complex.ONE.minus(erf(z));
    }

    public static Complex erfi(Complex z) {
        return erf(z.timesI()).timesNegativeI();
    }

    public static void main(String[] args) {
        for (double re = -2; re <= 2; re += 0.1) {
            for (double im = -2; im <= 2; im += 0.1) {
                final Complex c = complex(re, im);
                final Complex z = erf(c);
                System.out.format("(%10.7f, %10.7f)  =>  (%10.7f, %10.7f)\n", c.re(), c.im(), z.re(), z.im());
            }
        }
    }
}
