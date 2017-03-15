package net.fwitz.math.complex.analysis;

import net.fwitz.math.complex.Complex;

import static java.lang.Math.PI;
import static net.fwitz.math.complex.Complex.ONE;
import static net.fwitz.math.complex.Complex.TWO;
import static net.fwitz.math.complex.Complex.ZERO;
import static net.fwitz.math.complex.Complex.complex;
import static net.fwitz.math.complex.Complex.real;
import static net.fwitz.math.complex.analysis.Gamma.gamma;

public class DirichletEta {
    private static final int DEFAULT_TERMS_NEAR_1 = 100;
    private static final int DEFAULT_TERMS = 10;
    private static final double MINUS_PI_OVER_2 = PI / -2;
    private static final Complex CPI = real(PI);

    public static Complex eta(Complex s) {
        final int terms = (Math.abs(s.re() - 1) <= 0.4) ? DEFAULT_TERMS_NEAR_1 : DEFAULT_TERMS;
        return eta(s, terms);
    }

    public static Complex eta(Complex s, int terms) {
        // eta(0) = 1/2
        if (ZERO.equals(s)) {
            return complex(0.5, 0);
        }

        // eta(-2n) = 0
        if (s.re() < 0 && s.im() == 0) {
            long reInt = Math.round(s.re());
            if (s.re() == reInt && (reInt & 1) == 0) {
                return ZERO;
            }
        }

        if (s.re() > 4) {
            return simpleAlternatingSum(s, terms);
        }

        if (s.re() > -4) {
            return eulerAcceleratedSum(s, terms);
        }

        return functionalEquation(s, terms);
    }

    // 1^-s - 2^-s + 3^-s - ...
    private static Complex simpleAlternatingSum(Complex s, int terms) {
        Complex minusS = s.negative();
        boolean negate = false;
        Complex sum = ZERO;
        for (int i = 1; i < terms; ++i) {
            Complex term = real(i).pow(minusS);
            sum = negate ? sum.minus(term) : sum.plus(term);
            negate = !negate;
        }
        return sum;
    }

    private static Complex eulerAcceleratedSum(Complex s, int terms) {
        Complex minusS = s.negative();
        Complex outerSum = ZERO;
        boolean negate = true;
        double coeff = 1;
        Complex innerSum = ONE;
        long nMinusJPlus1 = terms;
        long j = 1L;

        for (int k = terms * terms; k > terms; --k) {
            Complex term = real(k).pow(minusS).times(innerSum);
            outerSum = negate ? outerSum.minus(term) : outerSum.plus(term);
            coeff = coeff * nMinusJPlus1 / j;
            innerSum = innerSum.plus(coeff);
            --nMinusJPlus1;
            ++j;
            negate = !negate;
        }

        outerSum = outerSum.div(Math.pow(2, terms));
        for (int k = terms; k > 0; --k) {
            Complex term = real(k).pow(minusS);
            outerSum = negate ? outerSum.minus(term) : outerSum.plus(term);
            negate = !negate;
        }

        return outerSum;
    }

    // eta(-s) = 2 * (1 - 2^(-s-1)) / (1 - 2^(-s)) * pi^(-s-1) * s * sin(pi * s / 2) * gamma(s) * eta(s + 1)
    // eta(s) = 2 * (1 - 2^(s-1)) / (1 - 2^s) * pi ^ (s-1) * (-s) * sin(s * -pi/2) * gamma(-s) * eta(-s + 1)
    private static Complex functionalEquation(Complex s, int terms) {
        Complex sMinus1 = s.minus(1);
        Complex minusS = s.negative();

        Complex fracTermNumer = ONE.minus(TWO.pow(sMinus1));
        Complex fracTermDenom = ONE.minus(TWO.pow(s));
        Complex piTerm = CPI.pow(sMinus1);
        Complex sinTerm = s.times(MINUS_PI_OVER_2).sin();
        Complex gammaTerm = gamma(minusS);
        Complex etaTerm = eta(minusS.plus(1), terms);

        return fracTermNumer.times(2)
                .div(fracTermDenom)
                .times(piTerm)
                .times(minusS)
                .times(sinTerm)
                .times(gammaTerm)
                .times(etaTerm);
    }

    public static void main(String[] args) {
        debug(-4, 0);
        debug(-3.5, 0);
        debug(-3, 0);
        debug(-1.5, 0);
        debug(-1, 0);
        debug(-0.5, 0);
        debug(0, 0);
        debug(0.5, 0);
        debug(1, 0);
        debug(1.5, 0);
        debug(2, 0);

        debug(-4, -0.5);
        debug(-3.5, -0.5);
        debug(-3, 0.5);
        debug(-1.5, 1);
        debug(-1, 1.5);
        debug(-0.5, 3);
        debug(0, 1.5);
        debug(0.5, 2);
        debug(1, 1.5);
        debug(1.5, 0.5);
        debug(2, -3);
    }

    private static void debug(double re, double im) {
        final Complex s = complex(re, im);
        final Complex etaS = eta(s);
        System.out.format("(%10.7f, %10.7f) => (%10.7f, %10.7f)\n", re, im, etaS.re(), etaS.im());
    }
}
