package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvancedPlusLogScale;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class SixthPower {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z ^ 6")
                .computeFn(z -> z.pow(6))
                .colorFn(new DomainColoringAdvancedPlusLogScale())
                .domainX(-1.5, 1.5)
                .domainY(-1.5, 1.5)
                .render();
    }
}
