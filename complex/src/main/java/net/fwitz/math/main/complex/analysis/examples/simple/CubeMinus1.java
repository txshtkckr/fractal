package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.FunctionPlot;

public class CubeMinus1 {
    public static void main(String[] args) {
        new FunctionPlot("z^3 - 1")
                .fn(z -> z.times(z).times(z).minus(1))
                .render();
    }
}
