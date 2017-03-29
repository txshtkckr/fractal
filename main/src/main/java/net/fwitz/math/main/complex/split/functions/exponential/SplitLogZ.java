package net.fwitz.math.main.complex.split.functions.exponential;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitLogZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("log(z)")
                .computeFn(SplitComplex::log)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
