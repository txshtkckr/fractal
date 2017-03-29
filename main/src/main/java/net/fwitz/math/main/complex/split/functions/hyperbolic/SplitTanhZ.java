package net.fwitz.math.main.complex.split.functions.hyperbolic;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitTanhZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("tanh(z)")
                .computeFn(SplitComplex::tanh)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
