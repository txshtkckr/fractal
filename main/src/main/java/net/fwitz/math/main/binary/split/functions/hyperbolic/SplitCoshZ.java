package net.fwitz.math.main.binary.split.functions.hyperbolic;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitCoshZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("cosh(z)")
                .computeFn(SplitComplex::cosh)
                .render();
    }
}
