package net.fwitz.math.main.complex.functions.exponential;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class ExpZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("exp(z)")
                .computeFn(Complex::exp)
                .render();
    }
}
