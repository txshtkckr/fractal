package net.fwitz.math.main.complex.functions.examples.simple;

import net.fwitz.math.plot.complex.color.DomainColoringAdvancedPlusLogScale;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class SixthPower {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z ^ 6")
                .computeFn(z -> z.pow(6))
                .colorFn(new DomainColoringAdvancedPlusLogScale())
                .domainRe(-1.5, 1.5)
                .domainIm(-1.5, 1.5)
                .render();
    }
}
