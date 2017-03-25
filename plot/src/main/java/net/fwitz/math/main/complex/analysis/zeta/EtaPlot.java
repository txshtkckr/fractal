package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.DirichletEta;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class EtaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("eta(s)")
                .computeFn(DirichletEta::eta)
                .size(400, 400)
                .domainRe(0.45, 0.55)
                .domainIm(30.4, 30.5)
                .render();
    }
}
