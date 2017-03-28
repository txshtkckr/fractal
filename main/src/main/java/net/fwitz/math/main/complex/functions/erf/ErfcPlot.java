package net.fwitz.math.main.complex.functions.erf;

import net.fwitz.math.complex.analysis.Erf;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class ErfcPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("erfc(z)")
                .computeFn(Erf::erfc)
                .domainRe(-5, 5)
                .domainIm(-5, 5)
                .render();
    }
}
