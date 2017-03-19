package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.FunctionPlot;

public class CubeMinus1NestedThreeTimes {
    public static void main(String[] args) {
        new FunctionPlot("((z^3 - 1)^3 - 1)^3 - 1")
                .fn(z -> z.pow(3).minus(1).pow(3).minus(1).pow(3).minus(1))
                .domainRe(0.5, 1.5)
                .domainIm(-0.5, 0.5)
                .render();
    }
}
