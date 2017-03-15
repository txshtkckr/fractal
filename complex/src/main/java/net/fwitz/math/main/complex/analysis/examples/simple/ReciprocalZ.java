package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class ReciprocalZ {
    public static void main(String[] args) {
        new FunctionPlot("1/z")
                .fn(Complex::inverse)
                .render();
    }
}
