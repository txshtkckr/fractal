package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.color.complex.domain.DomainColoringAdvanced;

public class ZetaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("zeta(s)")
                .domainRe(0.45, 0.55)
                .domainIm(30.4, 30.5)
                .size(800, 800)
                .computeFn(z -> RiemannZeta.zeta(z, 14))
                .colorFn(new DomainColoringAdvanced())
                .render();
    }
}
