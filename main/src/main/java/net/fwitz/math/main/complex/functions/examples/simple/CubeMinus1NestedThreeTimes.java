package net.fwitz.math.main.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class CubeMinus1NestedThreeTimes {
    public static void main(String[] args) {
        new ComplexFunctionPlot("((z^3 - 1)^3 - 1)^3 - 1")
                .computeFn(z -> z.pow3().minus(1).pow3().minus(1).pow3().minus(1))
                .domainX(0.5, 1.5)
                .domainY(-0.5, 0.5)
                .render();
    }
}
