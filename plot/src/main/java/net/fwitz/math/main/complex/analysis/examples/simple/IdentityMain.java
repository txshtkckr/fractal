package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.color.complex.domain.DomainColoringAdvancedPlusLogScale;

public class IdentityMain {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z")
                .computeFn(z -> z)
                .colorFn(new DomainColoringAdvancedPlusLogScale())
                .render();
    }
}
