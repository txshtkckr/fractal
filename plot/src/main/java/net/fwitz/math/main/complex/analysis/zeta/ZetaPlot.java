package net.fwitz.math.main.complex.analysis.zeta;

import net.fwitz.math.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringAdvanced;

public class ZetaPlot {
    public static void main(String[] args) {
        new FunctionPlot("zeta(s)")
                .domainRe(-0.02, 1.02)
                .domainIm(100.98, 102.02)
                .size(800, 800)
                .fn(z -> RiemannZeta.zeta(z, 14))
                .colorFn(new DomainColoringAdvanced())
                .render();
    }
}
