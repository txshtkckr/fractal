package net.fwitz.math.main.binary.complex.analysis.zeta;

import net.fwitz.math.binary.complex.analysis.RiemannZeta;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;
import net.fwitz.math.plot.binary.complex.color.DomainColoringContour;

public class ZetaPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("zeta(s)")
                .size(500, 1000)
                .domainX(-10, 10)
                .domainY(-20, 20)
                .computeFn(RiemannZeta::zeta)
                //.colorFn(new DomainColoringZeroBasin())
                .colorFn(new DomainColoringContour())
                .render();
    }
}
