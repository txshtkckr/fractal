package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.binary.complex.color.DomainColoringContour;

public class IdentityMain {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z")
                .computeFn(z -> z)
                .colorFn(new DomainColoringContour())
                .render();
    }
}
