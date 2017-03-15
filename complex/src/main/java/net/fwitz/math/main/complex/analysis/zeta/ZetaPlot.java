package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.FunctionPlot;

public class ZetaPlot {
    public static void main(String[] args) {
        new FunctionPlot("zeta(s)")
                .domainRe(-2, 2)
                .domainIm(12, 16)
                .size(200, 200)
                .fn(RiemannZeta::zeta)
                .render();
    }
}
