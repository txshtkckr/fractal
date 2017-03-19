package net.fwitz.math.complex.analysis;

import net.fwitz.math.complex.Complex;

import static net.fwitz.math.complex.Complex.complex;
import static net.fwitz.math.complex.analysis.DirichletEta.eta;
import static net.fwitz.math.complex.analysis.Gamma.gamma;

public class RiemannZeta {
    private static final Complex MINUS_ONE_HALF = complex(-0.5, 0.0);
    private static final double PI_OVER_2 = Math.PI / 2;

    public static Complex zeta(Complex s) {
        if (s.equals(Complex.ZERO)) {
            return MINUS_ONE_HALF;
        }

        if (s.equals(Complex.ONE)) {
            return Complex.POSITIVE_RE_INFINITY;
        }

        if (s.re() > 0) {
            return scaledEtaFunction(s);
        }

        return functionalEquation(s);
    }

    public static Complex zeta(Complex s, int terms) {
        if (s.equals(Complex.ZERO)) {
            return MINUS_ONE_HALF;
        }

        if (s.equals(Complex.ONE)) {
            return Complex.POSITIVE_RE_INFINITY;
        }

        if (s.re() > 0) {
            return scaledEtaFunction(s, terms);
        }

        return functionalEquation(s, terms);
    }

    public static Complex xi(Complex s) {
        Complex oneHalfS = s.times(0.5);
        return oneHalfS
                .times(s.minus(1))
                .times(Complex.PI.pow(oneHalfS.negative()))
                .times(gamma(oneHalfS))
                .times(zeta(s));
    }

    public static Complex xi(Complex s, int terms) {
        Complex oneHalfS = s.times(0.5);
        return oneHalfS
                .times(s.minus(1))
                .times(Complex.PI.pow(oneHalfS.negative()))
                .times(gamma(oneHalfS))
                .times(zeta(s, terms));
    }

    // zeta(S) = eta(s) / (1 - 2^(1-s))
    private static Complex scaledEtaFunction(Complex s) {
        Complex twoToOneMinusS = Complex.TWO.pow(Complex.ONE.minus(s));
        Complex scale = Complex.ONE.minus(twoToOneMinusS);
        return eta(s).div(scale);
    }

    private static Complex scaledEtaFunction(Complex s, int terms) {
        Complex twoToOneMinusS = Complex.TWO.pow(Complex.ONE.minus(s));
        Complex scale = Complex.ONE.minus(twoToOneMinusS);
        return eta(s, terms).div(scale);
    }

    // 2^s * pi^(s-1) * sin(pi * s / 2) * gamma(1 - s)
    private static Complex functionalCoefficient(Complex s, Complex sMinusOne, Complex oneMinusS) {
        return Complex.TWO.pow(s)
                .times(Complex.PI.pow(sMinusOne))
                .times(s.times(PI_OVER_2).sin())
                .times(gamma(oneMinusS));
    }

    // zeta(s) = 2^s * pi^(s-1) * sin(pi * s / 2) * gamma(1 - s) * zeta(1 - s)
    // zeta(s) = xi(s) * zeta(1-s)
    private static Complex functionalEquation(Complex s) {
        Complex sMinusOne = s.minus(1);
        Complex oneMinusS = sMinusOne.negative();
        return functionalCoefficient(s, sMinusOne, oneMinusS)
                .times(zeta(oneMinusS));
    }

    private static Complex functionalEquation(Complex s, int terms) {
        Complex sMinusOne = s.minus(1);
        Complex oneMinusS = sMinusOne.negative();
        return functionalCoefficient(s, sMinusOne, oneMinusS)
                .times(zeta(oneMinusS, terms));
    }

    public static void main(String[] args) {
        Complex z = complex(0.5, 102);
        for (int i = 1; i < 100; ++i) {
            final Complex eta = eta(z, i);
            final Complex zeta = zeta(z, i);
            System.out.format("%4d:  eta(z)=(%10.7f, %10.7f)  zeta(z)=(%10.7f, %10.7f)\n",
                    i, eta.re(), eta.im(), zeta.re(), zeta.im());
        }
    }
}
