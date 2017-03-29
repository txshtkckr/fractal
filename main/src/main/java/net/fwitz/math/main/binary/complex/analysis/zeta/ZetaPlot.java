package net.fwitz.math.main.binary.complex.analysis.zeta;

import net.fwitz.math.binary.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.binary.complex.color.DomainColoringZeroBasin;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class ZetaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("zeta(s)")
                .domainX(-5.0, 5.0)
                .domainY(280.0, 285.0)
                .size(800, 800)
                .computeFn(z -> RiemannZeta.zeta(z, 14))
                .colorFn(new DomainColoringZeroBasin())
                .render();
    }
}
