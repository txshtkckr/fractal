package net.fwitz.math.main.binary.complex.functions.hyperbolic;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class CoshZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("cosh(z)")
                .computeFn(Complex::cosh)
                .render();
    }
}
