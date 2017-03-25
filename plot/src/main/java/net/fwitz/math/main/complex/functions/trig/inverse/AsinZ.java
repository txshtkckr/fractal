package net.fwitz.math.main.complex.functions.trig.inverse;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class AsinZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("asin(z)")
                .computeFn(Complex::asin)
                .render();
    }
}
