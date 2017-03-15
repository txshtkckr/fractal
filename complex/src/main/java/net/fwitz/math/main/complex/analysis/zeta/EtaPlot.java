package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.DirichletEta;
import net.fwitz.math.plot.FunctionPlot;

public class EtaPlot {
    public static void main(String[] args) {
        new FunctionPlot("eta(s)")
                .fn(DirichletEta::eta)
                .size(400, 400)
                .domainRe(-2, 2)
                .domainIm(12, 16)
                .render();
    }
}
