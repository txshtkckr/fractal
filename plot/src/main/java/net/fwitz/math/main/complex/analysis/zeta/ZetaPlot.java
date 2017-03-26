package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.color.complex.domain.DomainColoringAdvanced;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class ZetaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("zeta(s)")
                .domainRe(0.4, 0.6)
                .domainIm(14.0, 14.2)
                .size(800, 800)
                .computeFn(z -> RiemannZeta.zeta(z, 14))
                .colorFn(new DomainColoringAdvanced())
                .render();
    }
}
