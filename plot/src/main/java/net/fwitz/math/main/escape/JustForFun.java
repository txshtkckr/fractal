package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.escape.EscapeTime;

import static net.fwitz.math.complex.Complex.complex;

public class JustForFun {
    private static final double P_MIN = -0.1573;
    private static final double P_MAX = -0.1574;
    private static final double Q_MIN = 0.5574;
    private static final double Q_MAX = 0.5575;
    private static final int ITERS = 50;

    public static void main(String[] args) {
        new FunctionPlot("z <= (sin(z) * cos(z))^pi + c (Escape time)")
                .fn(JustForFun::fn)
                .domainBound(P_MIN, Q_MIN, P_MAX, Q_MAX)
                .colorFn(new EscapeTime())
                .render();
    }

    private static Complex fn(Complex c) {
        Complex z = Complex.ZERO;
        for (int i = 1; i <= ITERS; ++i) {
            z = z.sin().times(z.cos()).pow(Math.PI).plus(c);
            if (z.abs() > 2.0) {
                return complex(i, ITERS);
            }
        }
        return complex(Double.NaN, ITERS);
    }
}
