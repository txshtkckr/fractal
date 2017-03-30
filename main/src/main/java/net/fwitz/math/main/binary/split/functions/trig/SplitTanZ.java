package net.fwitz.math.main.binary.split.functions.trig;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;

public class SplitTanZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("tan(z)")
                .computeFn(SplitComplex::tan)
                .render();
    }
}
