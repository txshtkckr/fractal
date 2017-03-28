package net.fwitz.math.main.complex.functions.examples.simple;

import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class SinReciprocalZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sin(1/z)")
                .computeFn(z -> z.inverse().sin())
                .render();
    }
}
