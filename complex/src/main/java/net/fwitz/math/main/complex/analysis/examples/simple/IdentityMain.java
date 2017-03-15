package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringAdvancedPlusLogScale;

public class IdentityMain {
    public static void main(String[] args) {
        new FunctionPlot("z")
                .fn(z -> z)
                .colorFn(new DomainColoringAdvancedPlusLogScale())
                .render();
    }
}
