package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.binary.complex.color.DomainColoringContour;

public class CubeMinus1OverZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("(z^3 - 1) / z")
                .computeFn(z -> z.pow3().minus(1).div(z))
                .domainX(-2.0, 2.0)
                .domainY(-2.0, 2.0)
                .colorFn(new DomainColoringContour())
                .render();
    }
}
