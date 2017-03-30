package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvanced;

public class ExpLogZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("e^(log z)")
                .computeFn(z -> z.log().exp())
                .colorFn(new DomainColoringAdvanced())
                .render();
    }
}
