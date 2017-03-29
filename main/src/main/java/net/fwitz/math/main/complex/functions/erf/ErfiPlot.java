package net.fwitz.math.main.complex.functions.erf;

import net.fwitz.math.complex.analysis.Erf;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class ErfiPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("erfi(z)")
                .computeFn(Erf::erfi)
                .domainX(-5, 5)
                .domainY(-5, 5)
                .render();
    }
}
