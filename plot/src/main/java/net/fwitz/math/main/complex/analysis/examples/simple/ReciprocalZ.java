package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class ReciprocalZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("1/z")
                .computeFn(Complex::inverse)
                .render();
    }
}
