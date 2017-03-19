package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.FunctionPlot;

public class XiPlot {
    public static void main(String[] args) {
        new FunctionPlot("xi(s)")
                .domainRe(-5, 5)
                .domainIm(-5, 5)
                .size(500, 500)
                .fn(RiemannZeta::xi)
                .render();
    }
}
