package net.fwitz.math.main.complex.functions.hyperbolic;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class CoshZ {
    public static void main(String[] args) {
        new FunctionPlot("cosh(z)")
                .fn(Complex::cosh)
                .render();
    }
}
