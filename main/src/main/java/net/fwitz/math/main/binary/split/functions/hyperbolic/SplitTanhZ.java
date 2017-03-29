package net.fwitz.math.main.binary.split.functions.hyperbolic;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitTanhZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("tanh(z)")
                .computeFn(SplitComplex::tanh)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
