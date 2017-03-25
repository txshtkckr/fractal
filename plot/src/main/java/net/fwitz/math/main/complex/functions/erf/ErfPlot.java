package net.fwitz.math.main.complex.functions.erf;

import net.fwitz.math.complex.analysis.Erf;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.color.complex.domain.DomainColoringSmooth;

public class ErfPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("erf(z)")
                .computeFn(Erf::erf)
                .domainRe(-5, 5)
                .domainIm(-5, 5)
                .colorFn(new DomainColoringSmooth())
                .render();
    }
}
