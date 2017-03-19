package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.escape.EscapeTime;

import static net.fwitz.math.complex.Complex.complex;

// https://www.flickr.com/photos/fractal_ken/3476756762
public class SignOfTheSunGod {

    private static final double P_MIN = -2.0;
    private static final double P_MAX = 1.0;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX = 1.5;
    private static final int ITERS = 50;

    public static void main(String[] args) {
        new FunctionPlot("z(n) = 1 / (z(n-1) + z(0))^2 (Escape time)")
                .fn(SignOfTheSunGod::fn)
                .domainBound(P_MIN, Q_MIN, P_MAX, Q_MAX)
                .colorFn(new EscapeTime())
                .render();
    }

    private static Complex fn(Complex c) {
        Complex z = Complex.ZERO;
        for (int i = 1; i <= ITERS; ++i) {
            z = z.plus(c).pow(-2);
            if (z.abs() > 10.0) {
                return complex(i, ITERS);
            }
        }
        return complex(Double.NaN, ITERS);
    }
}
