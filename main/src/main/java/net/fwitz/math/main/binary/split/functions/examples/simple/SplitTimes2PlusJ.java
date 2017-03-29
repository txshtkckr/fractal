package net.fwitz.math.main.binary.split.functions.examples.simple;

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

import static net.fwitz.math.binary.split.SplitComplex.splitComplex;

public class SplitTimes2PlusJ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("f(z) = z * (2 + j)")
                .computeFn(z -> z.times(splitComplex(2, 1)))
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
