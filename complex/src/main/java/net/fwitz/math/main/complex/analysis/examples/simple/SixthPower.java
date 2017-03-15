package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringAdvancedPlusLogScale;

public class SixthPower {
    public static void main(String[] args) {
        new FunctionPlot("z ^ 6")
                .fn(z -> z.pow(6))
                .colorFn(new DomainColoringAdvancedPlusLogScale())
                .domainRe(-1.5, 1.5)
                .domainIm(-1.5, 1.5)
                .render();
    }
}
