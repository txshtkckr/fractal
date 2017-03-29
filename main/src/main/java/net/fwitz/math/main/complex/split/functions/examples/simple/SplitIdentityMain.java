package net.fwitz.math.main.complex.split.functions.examples.simple;

import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitIdentityMain {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("z")
                .computeFn(z -> z)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
