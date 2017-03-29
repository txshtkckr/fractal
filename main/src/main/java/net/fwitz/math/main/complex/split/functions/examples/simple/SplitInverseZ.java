package net.fwitz.math.main.complex.split.functions.examples.simple;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitInverseZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("1/z")
                .computeFn(SplitComplex::inverse)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
