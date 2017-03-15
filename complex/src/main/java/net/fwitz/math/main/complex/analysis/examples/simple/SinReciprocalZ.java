package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.FunctionPlot;

public class SinReciprocalZ {
    public static void main(String[] args) {
        new FunctionPlot("sin(1/z)")
                .fn(z -> z.inverse().sin())
                .render();
    }
}
