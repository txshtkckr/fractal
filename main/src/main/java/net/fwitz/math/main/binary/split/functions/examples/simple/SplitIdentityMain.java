package net.fwitz.math.main.binary.split.functions.examples.simple;

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitIdentityMain {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("z")
                .computeFn(z -> z)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
