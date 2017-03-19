package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

import static java.lang.Math.abs;
import static net.fwitz.math.complex.Complex.complex;

public class BurningShipPlot {
    public static void main(String[] args) {
        new FunctionPlot("Burning Ship (Escape time)")
                .fn(BurningShipPlot::fn)
                .render();
    }

    private static Complex fn(Complex c) {
        return MandelbrotPlot.fn(c, BurningShipPlot::rectify);
    }

    private static Complex rectify(Complex z) {
        return complex(abs(z.re()), abs(z.im()));
    }
}
