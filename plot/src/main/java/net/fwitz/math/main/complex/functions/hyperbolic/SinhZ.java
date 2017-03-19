package net.fwitz.math.main.complex.functions.hyperbolic;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class SinhZ {
    public static void main(String[] args) {
        new FunctionPlot("sinh(z)")
                .fn(Complex::sinh)
                .render();
    }
}
