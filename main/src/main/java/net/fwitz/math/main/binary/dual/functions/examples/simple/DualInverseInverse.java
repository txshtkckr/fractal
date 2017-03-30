package net.fwitz.math.main.binary.dual.functions.examples.simple;

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualInverseInverse {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("1 / (1/z)")
                .computeFn(z -> z.inverse().inverse())
                .render();
    }
}
