package net.fwitz.math.main.binary.split.functions.trig;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitCosZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("cos(z)")
                .computeFn(SplitComplex::cos)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
