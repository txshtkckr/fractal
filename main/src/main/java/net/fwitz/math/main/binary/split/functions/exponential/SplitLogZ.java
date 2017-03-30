package net.fwitz.math.main.binary.split.functions.exponential;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitLogZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("log(z)")
                .computeFn(SplitComplex::log)
                .render();
    }
}
