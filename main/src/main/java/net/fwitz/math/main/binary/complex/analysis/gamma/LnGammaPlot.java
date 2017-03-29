package net.fwitz.math.main.binary.complex.analysis.gamma;

import net.fwitz.math.binary.complex.analysis.Gamma;
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class LnGammaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("lnGamma(z)")
                .computeFn(Gamma::lnGamma)
                .colorFn(new DomainColoringHanlu())
                .render();
    }
}
