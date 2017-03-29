package net.fwitz.math.binary.complex.analysis;

import net.fwitz.math.binary.complex.Complex;

import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static net.fwitz.math.binary.complex.Complex.complex;

public class Gamma {
    private static final double HALF_LN_2PI = 0.5 * log(PI * 2);
    private static final double ONE_TWELFTH = 1.0 / 12;

    // The coefficients to apply to the negative odd powers of Z, starting with z^-3
    private static final double[] COEFFICIENTS = {
            -1.0 / 360,
            1.0 / 1260,
            -1.0 / 1680,
            1.0 / 1188,
            -691.0 / 360360,
            1.0 / 156,
            -3617.0 / 122400
    };

    public static double gamma(double x) {
        return Math.exp(lnGamma(x));
    }

    public static Complex gamma(Complex z) {
        final Complex lnGamma = lnGamma(z);
        if (lnGamma.x() >= 75.0) {
            return Complex.POSITIVE_RE_INFINITY;
        }
        if (lnGamma.x() < -200.0) {
            return Complex.ZERO;
        }
        return lnGamma.exp();
    }


    public static double lnGamma(double re) {
        return lnGamma(re, 0).x();
    }

    public static Complex lnGamma(double re, double im) {
        return lnGamma(complex(re, im));
    }

    public static Complex lnGamma(Complex z) {
        // For negative integers, the value is infinite
        if (z.x() <= 0.0 && z.y() == 0.0 && z.x() == floor(z.x())) {
            return Complex.POSITIVE_RE_INFINITY;
        }

        // Use conjugate relationship to avoid working in 3rd or 4th quad directly
        if (z.y() < 0.0) {
            return lnGamma(z.conjugate()).conjugate();
        }

        Complex adjust = Complex.ZERO;
        Complex temp = z;

        // The analysis is only considered valid for the domain 9 <= Re(z) <= 10
        // If we are outside this range, then repeatedly apply the relationship
        //    lnGamma(z + 1) = ln(z) + lnGamma(z)
        // Until we are within the allowed domain
        while (temp.x() < 9.0) {
            // Perform lnGamma on a larger value, then subtract the excess ln(z)
            adjust = adjust.minus(temp.log());
            temp = temp.plus(1.0);
        }

        // If the value is to the right of the good range for this analysis, then compensate
        while (temp.x() > 10.0) {
            // Perform lnGamma on a smaller value, then add the missing ln(z - 1)
            temp = temp.minus(1.0);
            adjust = adjust.plus(temp.log());
        }

        return lnGammaStirling(temp).plus(adjust);
    }

    private static Complex lnGammaStirling(Complex z) {
        Complex temp1 = z.log();
        Complex temp2 = z.minus(0.5);

        Complex result = temp1.times(temp2);
        result = result.minus(z);
        result = result.plus(HALF_LN_2PI);

        Complex zPower = z.inverse();
        final Complex oneOverZ2 = zPower.times(zPower);
        result = result.plus(zPower.times(ONE_TWELFTH));

        for (double coefficient : COEFFICIENTS) {
            zPower = zPower.times(oneOverZ2);
            result = result.plus(zPower.times(coefficient));
        }

        return result;
    }
}
