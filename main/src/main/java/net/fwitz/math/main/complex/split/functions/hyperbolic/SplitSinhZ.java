package net.fwitz.math.main.complex.split.functions.hyperbolic;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitSinhZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("sinh(z)")
                .computeFn(SplitComplex::sinh)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
