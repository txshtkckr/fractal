package net.fwitz.math.main.complex.split.functions.hyperbolic;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitCoshZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("cosh(z)")
                .computeFn(SplitComplex::cosh)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
