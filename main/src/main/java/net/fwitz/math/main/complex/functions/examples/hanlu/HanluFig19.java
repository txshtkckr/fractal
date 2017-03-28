package net.fwitz.math.main.complex.functions.examples.hanlu;

import net.fwitz.math.plot.complex.color.DomainColoringHanlu;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class HanluFig19 {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sin(1/z)")
                .computeFn(z -> z.inverse().sin())
                .domainBound(-1, -1, 1, 1)
                .colorFn(new DomainColoringHanlu())
                .render();
    }
}