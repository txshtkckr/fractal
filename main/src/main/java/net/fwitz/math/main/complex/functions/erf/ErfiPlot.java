package net.fwitz.math.main.complex.functions.erf;

import net.fwitz.math.complex.analysis.Erf;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class ErfiPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("erfi(z)")
                .computeFn(Erf::erfi)
                .domainRe(-5, 5)
                .domainIm(-5, 5)
                .render();
    }
}
