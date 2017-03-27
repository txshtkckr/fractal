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
    private static final int DEFAULT_TERMS = 16;
    private static final int DEFAULT_TERMS_BOOST_NEAR_1 = 84;
    private static final double MINUS_PI_OVER_2 = PI / -2;
    private static final Complex CPI = real(PI);

    public static Complex eta(Complex s) {
        final double closeness = 1.0 - Math.abs(s.re() - 1);
        int terms = DEFAULT_TERMS;
        if (closeness > 0.0) {
            // & ~1 because we need to keep the term count even because we are oscillating as we converge to the
            // correct value, and the sign flip would cause extra noise
            terms += ((int) (DEFAULT_TERMS_BOOST_NEAR_1 * closeness)) & ~1;
        }
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

        if (s.re() > -4) {
            return eulerAcceleratedSum(s, terms);
        }

        return functionalEquation(s, terms);
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
        final Complex s = complex(0.5, 25.010858);
        for (int terms = 1; terms <= 20; ++terms) {
            debug(terms, s);
        }
    }

    private static void debug(int terms, Complex s) {
        final Complex etaS = eta(s);
        System.out.format("%4d: (%10.7f, %10.7f) => (%10.7f, %10.7f)\n", terms, s.re(), s.im(), etaS.re(), etaS.im());
    }
}
