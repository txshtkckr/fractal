package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.DirichletEta;
import net.fwitz.math.plot.complex.color.DomainColoringSmooth;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class EtaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("eta(s)")
                .computeFn(DirichletEta::eta)
                .size(500, 1000)
                .domainRe(-10, 10)
                .domainIm(-20, 20)
                .colorFn(new DomainColoringSmooth())
                .render();
    }
}
