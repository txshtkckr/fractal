package net.fwitz.math.main.binary.split.functions.examples.simple;

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitIdentityMain {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("z")
                .computeFn(z -> z)
                .render();
    }
}
