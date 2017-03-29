package net.fwitz.math.main.binary.complex.analysis.gamma;

import net.fwitz.math.binary.complex.analysis.Gamma;
import net.fwitz.math.plot.binary.complex.color.DomainColoringSmooth;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class GammaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("gamma(z)")
                .computeFn(Gamma::gamma)
                .colorFn(new DomainColoringSmooth())
                .render();
    }
}
