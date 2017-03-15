package net.fwitz.math.main.complex.analysis.gamma;

import net.fwitz.math.complex.analysis.Gamma;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringSmooth;

public class GammaPlot {
    public static void main(String[] args) {
        new FunctionPlot("gamma(z)")
                .fn(Gamma::gamma)
                .colorFn(new DomainColoringSmooth())
                .render();
    }
}
