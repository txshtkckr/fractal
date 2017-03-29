package net.fwitz.math.main.binary.complex.functions.hyperbolic;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class SinhZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sinh(z)")
                .computeFn(Complex::sinh)
                .render();
    }
}
