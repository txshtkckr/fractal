package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.complex.color.DomainColoringZeroBasin;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class ZetaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("zeta(s)")
                .domainRe(-5.0, 5.0)
                .domainIm(280.0, 285.0)
                .size(800, 800)
                .computeFn(z -> RiemannZeta.zeta(z, 14))
                .colorFn(new DomainColoringZeroBasin())
                .render();
    }
}
