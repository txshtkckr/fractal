package net.fwitz.math.main.complex.functions.erf;

import net.fwitz.math.complex.analysis.Erf;
import net.fwitz.math.plot.FunctionPlot;

public class ErfPlot {
    public static void main(String[] args) {
        new FunctionPlot("erf(z)")
                .fn(Erf::erf)
                .domainRe(-2, 2)
                .domainIm(-2, 2)
                .render();
    }
}
