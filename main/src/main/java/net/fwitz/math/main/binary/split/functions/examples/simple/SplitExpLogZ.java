package net.fwitz.math.main.binary.split.functions.examples.simple;

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitExpLogZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("e^(log z)")
                .computeFn(w -> w.region1mapped(z -> z.log().exp()))
                .render();
    }
}
