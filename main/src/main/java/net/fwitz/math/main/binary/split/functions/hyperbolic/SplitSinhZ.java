package net.fwitz.math.main.binary.split.functions.hyperbolic;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitSinhZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("sinh(z)")
                .computeFn(SplitComplex::sinh)
                .render();
    }
}
