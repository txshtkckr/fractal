package net.fwitz.math.main.binary.split.functions.hyperbolic;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitTanhZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("tanh(z)")
                .computeFn(SplitComplex::tanh)
                .render();
    }
}
