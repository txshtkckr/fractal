package net.fwitz.math.main.complex.split.functions.examples.simple;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitZ2 {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("z^2")
                .computeFn(SplitComplex::pow2)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
