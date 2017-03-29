package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class SinReciprocalZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sin(1/z)")
                .computeFn(z -> z.inverse().sin())
                .render();
    }
}
