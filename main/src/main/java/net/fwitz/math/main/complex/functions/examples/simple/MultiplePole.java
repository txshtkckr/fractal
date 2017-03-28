package net.fwitz.math.main.complex.functions.examples.simple;

import net.fwitz.math.plot.complex.color.DomainColoringAdvancedPlusLogScale;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class MultiplePole {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z ^ -3")
                .computeFn(z -> z.pow(-3.0))
                .colorFn(new DomainColoringAdvancedPlusLogScale())
                .domainRe(-1.5, 1.5)
                .domainIm(-1.5, 1.5)
                .render();
    }
}
