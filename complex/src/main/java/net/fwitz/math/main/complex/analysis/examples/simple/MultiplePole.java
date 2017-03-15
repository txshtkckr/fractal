package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringAdvancedPlusLogScale;

public class MultiplePole {
    public static void main(String[] args) {
        new FunctionPlot("z ^ -3")
                .fn(z -> z.pow(-3.0))
                .colorFn(new DomainColoringAdvancedPlusLogScale())
                .domainRe(-1.5, 1.5)
                .domainIm(-1.5, 1.5)
                .render();
    }
}
