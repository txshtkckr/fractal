package net.fwitz.math.main.binary.split.functions.trig;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitSinZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("sin(z)")
                .computeFn(SplitComplex::sin)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
