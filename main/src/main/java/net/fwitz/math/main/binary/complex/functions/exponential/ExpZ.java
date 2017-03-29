package net.fwitz.math.main.binary.complex.functions.exponential;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class ExpZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("exp(z)")
                .computeFn(Complex::exp)
                .render();
    }
}
