package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class CubeMinus1 {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z^3 - 1")
                .computeFn(z -> z.pow3().minus(1))
                .domainX(-1.5, 1.5)
                .domainY(-1.5, 1.5)
                .render();
    }
}
