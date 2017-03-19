package net.fwitz.math.main.complex.functions.hyperbolic;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class TanhZ {
    public static void main(String[] args) {
        new FunctionPlot("tanh(z)")
                .fn(Complex::tanh)
                .render();
    }
}
