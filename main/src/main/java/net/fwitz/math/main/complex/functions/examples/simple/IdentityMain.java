package net.fwitz.math.main.complex.functions.examples.simple;

import net.fwitz.math.plot.complex.color.DomainColoringAdvanced;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class IdentityMain {
    public static void main(String[] args) {
        new ComplexFunctionPlot("z")
                .computeFn(z -> z)
                .colorFn(new DomainColoringAdvanced())
                .render();
    }
}
