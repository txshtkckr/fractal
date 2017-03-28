package net.fwitz.math.main.complex.analysis.gamma;

import net.fwitz.math.complex.analysis.Gamma;
import net.fwitz.math.plot.complex.color.DomainColoringHanlu;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class LnGammaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("lnGamma(z)")
                .computeFn(Gamma::lnGamma)
                .colorFn(new DomainColoringHanlu())
                .render();
    }
}
