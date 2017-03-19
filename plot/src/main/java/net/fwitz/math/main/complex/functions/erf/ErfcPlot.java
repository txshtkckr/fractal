package net.fwitz.math.main.complex.functions.erf;

import net.fwitz.math.complex.analysis.Erf;
import net.fwitz.math.plot.FunctionPlot;

public class ErfcPlot {
    public static void main(String[] args) {
        new FunctionPlot("erfc(z)")
                .fn(Erf::erfc)
                .domainRe(-5, 5)
                .domainIm(-5, 5)
                .render();
    }
}
