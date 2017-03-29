package net.fwitz.math.main.complex.split.functions.trig;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitCosZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("cos(z)")
                .computeFn(SplitComplex::cos)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
