package net.fwitz.math.main.binary.split.functions.trig;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitSinZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("sin(z)")
                .computeFn(SplitComplex::sin)
                .render();
    }
}
