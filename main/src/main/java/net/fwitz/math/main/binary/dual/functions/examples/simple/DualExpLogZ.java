package net.fwitz.math.main.binary.dual.functions.examples.simple;

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualExpLogZ {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("e^(log z)")
                .computeFn(w -> w.region1mapped(z -> z.log().exp()))
                .render();
    }
}
