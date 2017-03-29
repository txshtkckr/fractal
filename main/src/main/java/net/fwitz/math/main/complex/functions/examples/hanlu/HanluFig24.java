package net.fwitz.math.main.complex.functions.examples.hanlu;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

import static net.fwitz.math.complex.Complex.complex;

public class HanluFig24 {
    private static final Complex ONE_PLUS_I = complex(1, 1);

    public static void main(String[] args) {
        new ComplexFunctionPlot("(1 + i)sin(z)")
                .computeFn(HanluFig24::iter)
                .colorFn(new DomainColoringHanlu())
                .render();
    }

    /**
     * <a href="http://users.mai.liu.se/hanlu09/complex/domain_coloring.html">Source</a>.
     */
    private static Complex iter(Complex z) {
        for (int i = 1; i <= 5; ++i) {
            z = fn(z);
        }
        return z;
    }

    private static Complex fn(Complex z) {
        return z.sin().times(ONE_PLUS_I);
    }
}
