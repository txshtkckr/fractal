package net.fwitz.math.main.binary.split.functions.trig;

import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitTanZ {
    public static void main(String[] args) {
        new SplitComplexFunctionPlot("tan(z)")
                .computeFn(SplitComplex::tan)
                .colorFn(new DomainColoringSplit())
                .render();
    }
}
