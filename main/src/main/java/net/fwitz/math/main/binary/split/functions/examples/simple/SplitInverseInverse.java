package net.fwitz.math.main.binary.split.functions.examples.simple;

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitInverseInverse {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("1 / (1/z)")
                .computeFn(z -> z.inverse().inverse())
                .render();
    }
}
