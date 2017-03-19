package net.fwitz.math.main.complex.analysis.examples.hanlu;

import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringHanlu;

public class HanluFig19 {
    public static void main(String[] args) {
        new FunctionPlot("sin(1/z)")
                .fn(z -> z.inverse().sin())
                .domainBound(-1, -1, 1, 1)
                .colorFn(new DomainColoringHanlu())
                .render();
    }
}
