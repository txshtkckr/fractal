package net.fwitz.math.main.binary.split.functions.examples.simple;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitInverseZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("1/z")
                .computeFn(SplitComplex::inverse)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
