package net.fwitz.math.main.binary.complex.functions.erf;

import net.fwitz.math.binary.complex.functions.Erf;
import net.fwitz.math.plot.binary.complex.color.DomainColoringSmooth;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class ErfPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("erf(z)")
                .computeFn(Erf::erf)
                .domainX(-5, 5)
                .domainY(-5, 5)
                .colorFn(new DomainColoringSmooth())
                .render();
    }
}
