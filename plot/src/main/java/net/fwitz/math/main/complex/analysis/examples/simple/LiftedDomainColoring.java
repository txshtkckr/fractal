package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

/**
 * Tasked from Figure 3 of
 * <a href="http://www.mi.fu-berlin.de/en/math/groups/ag-geom/publications/db/lifteddomaincoloring.pdf">this</a>
 * article.  {@code [(z−1)(z+1)^2] / [(z+i)(z−i)^2]}
 */
public class LiftedDomainColoring {
    public static void main(String[] args) {
        new FunctionPlot("[(z−1)(z+1)^2] / [(z+i)(z−i)^2]")
                .fn(LiftedDomainColoring::fn)
                .render();
    }

    private static Complex fn(Complex z) {
        return z.minus(1).times(z.plus(1).pow(2))
                .div(z.plusI(1).times(z.minusI(1).pow(2)));
    }
}
