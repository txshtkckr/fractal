package net.fwitz.math.main.complex.functions.examples.simple;

import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class CubeMinus1 {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z^3 - 1")
                .computeFn(z -> z.pow3().minus(1))
                .domainBound(-1.5, -1.5, 1.5, 1.5)
                .render();
    }
}
