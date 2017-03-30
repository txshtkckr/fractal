package net.fwitz.math.main.binary.split.functions.exponential;

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitExp1OverZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("exp(1/z)")
                .computeFn(z -> z.inverse().exp())
                .render();
    }
}
