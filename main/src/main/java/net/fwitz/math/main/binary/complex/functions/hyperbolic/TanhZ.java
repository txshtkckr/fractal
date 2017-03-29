package net.fwitz.math.main.binary.complex.functions.hyperbolic;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class TanhZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("tanh(z)")
                .computeFn(Complex::tanh)
                .render();
    }
}
