package net.fwitz.math.main.complex.analysis.gamma;

import net.fwitz.math.complex.analysis.Gamma;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.color.complex.domain.DomainColoringSmooth;

public class GammaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("gamma(z)")
                .computeFn(Gamma::gamma)
                .colorFn(new DomainColoringSmooth())
                .render();
    }
}
