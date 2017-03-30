package net.fwitz.math.main.binary.dual.functions.examples.simple;

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualIdentityMain {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("z")
                .computeFn(z -> z)
                .render();
    }
}
