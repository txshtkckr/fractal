package net.fwitz.math.main.binary.complex.functions.trig.inverse;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class AtanZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("atan(z)")
                .computeFn(Complex::atan)
                .render();
    }
}
