package net.fwitz.math.main.binary.complex.analysis.zeta;

import net.fwitz.math.binary.complex.analysis.DirichletEta;
import net.fwitz.math.plot.binary.complex.color.DomainColoringSmooth;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class EtaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("eta(s)")
                .computeFn(DirichletEta::eta)
                .size(500, 1000)
                .domainX(-10, 10)
                .domainY(-20, 20)
                .colorFn(new DomainColoringSmooth())
                .render();
    }
}
