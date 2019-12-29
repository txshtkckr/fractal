package net.fwitz.math.binary.complex.analysis;

import net.fwitz.math.binary.complex.Complex;

import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static net.fwitz.math.binary.complex.Complex.real;

public class Gamma {
    private static final double HALF_LN_2PI = 0.5 * log(PI * 2);
    private static final double ONE_TWELFTH = 1.0 / 12;
    private static final double SQRT_2PI = Math.sqrt(2 * Math.PI);
    private static final double[] LANCZOS_P = {
            0.99999999999980993,
            676.5203681218851,
            -1259.1392167224028,
            771.32342877765313,
            -176.61502916214059,
            12.507343278686905,
            -0.13857109526572012,
            9.9843695780195716e-6,
            1.5056327351493116e-7
    };

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
        return gammaLanczos(x);
    }

    public static Complex gamma(Complex z) {
        return gammaLanczos(z);
    }


    public static double lnGamma(double x) {
        // For non-positive integers, the value is infinite
        if (x <= 0.0 && x == floor(x)) {
            return Double.POSITIVE_INFINITY;
        }

        // Too far outside the range for stirling; use Lanczos
        if (x <= -11 || x >= 30) {
            return Math.log(gammaLanczos(x));
        }

        double adjust = 0.0;
        double temp = x;

        // The analysis is only considered valid for the domain 9 <= Re(z) <= 10
        // If we are outside this range, then repeatedly apply the relationship
        //    lnGamma(z + 1) = ln(z) + lnGamma(z)
        // Until we are within the allowed domain
        while (x < 9.0) {
            // Perform lnGamma on a larger value, then subtract the excess ln(z)
            adjust -= Math.log(temp);
            temp += 1.0;
        }

        // If the value is to the right of the good range for this analysis, then compensate
        while (x > 10.0) {
            // Perform lnGamma on a smaller value, then add the missing ln(z - 1)
            temp -= 1.0;
            adjust += Math.log(temp);
        }

        return lnGammaStirling(temp) + adjust;
    }

    public static Complex lnGamma(Complex z) {
        if (z.y() == 0.0) {
            return real(lnGamma(z.x()));
        }

        // Use conjugate relationship to avoid working in 3rd or 4th quad directly
        if (z.y() < 0.0) {
            return lnGamma(z.conjugate()).conjugate();
        }

        // Too far outside the range for stirling; use Lanczos
        if (z.x() <= -11 || z.x() >= 30) {
            return gammaLanczos(z).log();
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

    public static double lnGammaStirling(double x) {
        double temp1 = Math.log(x);
        double temp2 = x - 0.5;

        double result = temp1 * temp2;
        result -= x;
        result += HALF_LN_2PI;

        double xPower = 1.0 / x;
        final double oneOverX2 = xPower * xPower;
        result += xPower * ONE_TWELFTH;

        for (double coefficient : COEFFICIENTS) {
            xPower *= oneOverX2;
            result += xPower * coefficient;
        }

        return result;
    }

    public static double gammaStirling(double x) {
        return Math.exp(lnGammaStirling(x));
    }

    public static Complex lnGammaStirling(Complex z) {
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

    public static Complex gammaStirling(Complex z) {
        final Complex lnGamma = lnGamma(z);
        if (lnGamma.x() >= 75.0) {
            return Complex.POSITIVE_RE_INFINITY;
        }
        if (lnGamma.x() < -200.0) {
            return Complex.ZERO;
        }
        return lnGamma.exp();
    }

    public static double gammaLanczos(double x) {
        if (x < 0.5) {
            return Math.PI / (Math.sin(Math.PI * x) * gammaLanczos(1 - x));
        }
        
        x -= 1.0;
        double a = LANCZOS_P[0];
        double t = x + 7.5;
        for (int i = 1; i < LANCZOS_P.length; ++i) {
            a += LANCZOS_P[i] / (x + i);
        }

        return SQRT_2PI * Math.pow(t, x + 0.5) * Math.exp(-t) * a;
    }

    public static Complex gammaLanczos(Complex z) {
        if (z.x() < 0.5) {
            Complex gammaOneMinusZ = gammaLanczos(Complex.ONE.minus(z));
            Complex sinPiZ = z.times(Math.PI).sin();
            return real(Math.PI)
                    .div(sinPiZ.times(gammaOneMinusZ));
        }
        
        z = z.minus(1);
        Complex a = real(LANCZOS_P[0]);
        Complex t = z.plus(LANCZOS_P.length - 1.5);
        for (int i = 1; i < LANCZOS_P.length; ++i) {
            Complex term = real(LANCZOS_P[i])
                    .div(z.plus(i));
            a = a.plus(term);
        }

        return t.pow(z.plus(0.5))
                .times(SQRT_2PI)
                .times(t.negative().exp())
                .times(a);
    }
}
