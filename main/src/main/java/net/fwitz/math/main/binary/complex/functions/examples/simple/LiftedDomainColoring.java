package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

/**
 * Tasked from Figure 3 of
 * <a href="http://www.mi.fu-berlin.de/en/math/groups/ag-geom/publications/db/lifteddomaincoloring.pdf">this</a>
 * article.  {@code [(z−1)(z+1)^2] / [(z+i)(z−i)^2]}
 */
public class LiftedDomainColoring {
    public static void main(String[] args) {
        new ComplexFunctionPlot("[(z−1)(z+1)^2] / [(z+i)(z−i)^2]")
                .computeFn(LiftedDomainColoring::fn)
                .render();
    }

    private static Complex fn(Complex z) {
        return z.minus(1).times(z.plus(1).pow(2))
                .div(z.plusY(1).times(z.minusY(1).pow(2)));
    }
}
