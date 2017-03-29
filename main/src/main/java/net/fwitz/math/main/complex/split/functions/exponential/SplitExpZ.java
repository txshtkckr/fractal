package net.fwitz.math.main.complex.split.functions.exponential;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitExpZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("exp(z)")
                .computeFn(SplitComplex::exp)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
