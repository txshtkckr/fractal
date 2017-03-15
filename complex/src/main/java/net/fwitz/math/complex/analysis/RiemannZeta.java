package net.fwitz.math.complex.analysis;

import net.fwitz.math.complex.Complex;

import static net.fwitz.math.complex.Complex.complex;
import static net.fwitz.math.complex.Complex.real;
import static net.fwitz.math.complex.analysis.DirichletEta.eta;
import static net.fwitz.math.complex.analysis.Gamma.gamma;

public class RiemannZeta {
    private static final Complex MINUS_ONE_HALF = complex(-0.5, 0.0);
    private static final double PI_OVER_2 = Math.PI / 2;
    private static final double ONE_OVER_TWO_PI = 0.5 / Math.PI;
    private static final double RIEMANN_SIEGEL_THRESHOLD = 5;

    public static Complex zeta(Complex s) {
        if (s.equals(Complex.ZERO)) {
            return MINUS_ONE_HALF;
        }

        if (s.equals(Complex.ONE)) {
            return Complex.POSITIVE_RE_INFINITY;
        }

        if (s.re() > 0) {
            if (s.re() <= 1.0 && Math.abs(s.im()) > RIEMANN_SIEGEL_THRESHOLD) {
                return riemannSiegelApproximation(s);
            }
            return scaledEtaFunction(s);
        }

        return functionalEquation(s);
    }

    // 2*pi*x*y = |t| for s = sigma + i*t
    // 0 <= sigma <= 1
    // zeta(s) = sum(n:1...x)[ 1/(n^s) ] + xi(s) * sum(n:1...y)[ 1/(n^(1-s)) ]
    // xi(s) = = 2^s * pi^(s-1) * sin(pi * s / 2) * gamma(1 - s)
    // traditionally, we pick x = y = sqrt(|t| / (2 * pi))
    private static Complex riemannSiegelApproximation(Complex s) {
        int x = (int) Math.floor(Math.sqrt(Math.abs(s.im()) * ONE_OVER_TWO_PI));

        Complex minusS = s.negative();
        Complex sMinusOne = s.minus(1);
        Complex oneMinusS = sMinusOne.negative();
        Complex sum1 = Complex.ZERO;
        Complex sum2 = Complex.ZERO;
        for (int n = 1; n <= x; ++n) {
            Complex cn = real(n);
            sum1 = sum1.plus(cn.pow(minusS));
            sum2 = sum2.plus(cn.pow(sMinusOne));
        }
        return sum1.plus(xi(s, sMinusOne, oneMinusS).times(sum2));
    }

    // zeta(S) = eta(s) / (1 - 2^(1-s))
    private static Complex scaledEtaFunction(Complex s) {
        Complex twoToOneMinusS = Complex.TWO.pow(Complex.ONE.minus(s));
        Complex scale = Complex.ONE.minus(twoToOneMinusS);
        return eta(s).div(scale);
    }

    // xi(s) = 2^s * pi^(s-1) * sin(pi * s / 2) * gamma(1 - s)
    public static Complex xi(Complex s) {
        Complex sMinusOne = s.minus(1);
        Complex oneMinusS = sMinusOne.negative();
        return xi(s, sMinusOne, oneMinusS);
    }

    private static Complex xi(Complex s, Complex sMinusOne, Complex oneMinusS) {
        Complex sinPiSOver2 = s.times(PI_OVER_2).sin();
        Complex gammaOneMinusS = gamma(oneMinusS);
        return Complex.TWO.pow(s)
                .times(Complex.PI.pow(sMinusOne))
                .times(sinPiSOver2)
                .times(gammaOneMinusS);
    }

    // zeta(s) = 2^s * pi^(s-1) * sin(pi * s / 2) * gamma(1 - s) * zeta(1 - s)
    // zeta(s) = xi(s) * zeta(1-s)
    private static Complex functionalEquation(Complex s) {
        Complex sMinusOne = s.minus(1);
        Complex oneMinusS = sMinusOne.negative();
        return xi(s, sMinusOne, oneMinusS).times(zeta(oneMinusS));
    }

    public static void main(String[] args) {
        debug(-13, 0);
        debug(-11, 0);
        debug(-9, 0);
        debug(-7, 0);
        debug(-5, 0);
        debug(-3, 0);
        debug(-1, 0);
        debug(0, 0);
        debug(0.5, 0);
        debug(1.0, 0.0);
        debug(1.5, 0.0);
        debug(2.0, 0.0);
        debug(3.0, 0.0);
        debug(4.0, 0.0);
        debug(5.0, 0.0);
        debug(6.0, 0.0);
        debug(7.0, 0.0);
        debug(8.0, 0.0);
        debug(9.0, 0.0);
    }

    private static void debug(double re, double im) {
        final Complex z = complex(re, im);
        final Complex etaZ = eta(z);
        final Complex zetaZ = zeta(z);
        System.out.format("z=(%10.7f, %10.7f)  eta(z)=(%10.7f, %10.7f)  zeta(z)=(%10.7f, %10.7f)\n",
                re, im, etaZ.re(), etaZ.im(), zetaZ.re(), zetaZ.im());
    }
}
