package net.fwitz.math.main.binary.split.functions.examples.simple;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitZ2 {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("z^2")
                .computeFn(SplitComplex::pow2)
                .render();
    }
}
