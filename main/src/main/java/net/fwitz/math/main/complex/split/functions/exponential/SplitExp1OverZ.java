package net.fwitz.math.main.complex.split.functions.exponential;

import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitExp1OverZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("exp(1/z)")
                .computeFn(z -> z.inverse().exp())
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
