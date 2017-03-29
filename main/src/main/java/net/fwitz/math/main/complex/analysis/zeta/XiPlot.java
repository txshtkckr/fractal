package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class XiPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("xi(s)")
                .domainX(-5, 5)
                .domainY(-5, 5)
                .size(500, 500)
                .computeFn(RiemannZeta::xi)
                .render();
    }
}
