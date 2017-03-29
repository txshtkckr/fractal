package net.fwitz.math.binary.complex.functions;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.binary.complex.analysis.Gamma;

import static net.fwitz.math.binary.complex.analysis.Gamma.gamma;

public class Factorial {
    public static double fact(double n) {
        return Gamma.gamma(n + 1);
    }

    public static Complex fact(Complex z) {
        return Gamma.gamma(z.plus(1));
    }

    public static double binom(double n, double k) {
        double nf = fact(n);
        double nmkf = fact(n - k);
        double kf = fact(k);
        return nf / (nmkf * kf);
    }

    public static Complex binom(Complex n, Complex k) {
        return fact(n)
                .div(fact(n.minus(k)))
                .div(fact(k));
    }
}
