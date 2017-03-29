package net.fwitz.math.main.binary.complex.functions.examples.hanlu;

import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class HanluFig19 {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sin(1/z)")
                .computeFn(z -> z.inverse().sin())
                .domainX(-1, 1)
                .domainY(-1, 1)
                .colorFn(new DomainColoringHanlu())
                .render();
    }
}
