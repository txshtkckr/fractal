package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.palette.PaletteVGA8bitRGB;

import java.util.function.Function;

import static net.fwitz.math.complex.Complex.complex;

public class MandelbrotPlot {
    private static final double P_MIN = -2;
    private static final double P_MAX = 1;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX =1.5;
    private static final int ITERS = 1000;

    public static void main(String[] args) {
        new FunctionPlot("Mandelbrot (Escape time)")
                .fn(MandelbrotPlot::fn)
                .domainBound(P_MIN, Q_MIN, P_MAX, Q_MAX)
                .colorFn(PaletteVGA8bitRGB.escapeTime())
                .render();
    }

    private static Complex fn(Complex c) {
        return fn(c, z -> z);
    }

    protected static Complex fn(Complex c, Function<Complex, Complex> fudgeFn) {
        Complex z = Complex.ZERO;
        for (int i = 1; i <= ITERS; ++i) {
            z = fudgeFn.apply(z);
            z = z.times(z).plus(c);
            if (z.abs() > 2.0) {
                return complex(i, ITERS);
            }
        }
        return complex(Double.NaN, ITERS);
    }
}
