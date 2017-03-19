package net.fwitz.math.main.complex.analysis.gamma;

import net.fwitz.math.complex.analysis.Gamma;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringHanlu;

public class LnGammaPlot {
    public static void main(String[] args) {
        new FunctionPlot("lnGamma(z)")
                .fn(Gamma::lnGamma)
                .colorFn(new DomainColoringHanlu())
                .render();
    }
}
