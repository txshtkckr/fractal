package net.fwitz.math.main.binary.dual.functions.exponential;

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualExp1OverZ {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("exp(1/z)")
                .computeFn(z -> z.inverse().exp())
                .render();
    }
}
