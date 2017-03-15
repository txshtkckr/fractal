package net.fwitz.math.main.complex.analysis.examples.hanlu;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringHanlu;

import static net.fwitz.math.complex.Complex.complex;

public class HanluFig8 {
    private static final Complex ONE_PLUS_2_I = complex(1, 2);
    public static void main(String[] args) {
        new FunctionPlot("(z+2)^2 (z−1−2i) (z+i)")
                .fn(HanluFig8::fn)
                .colorFn(new DomainColoringHanlu())
                .render();
    }

    /** <a href="http://users.mai.liu.se/hanlu09/complex/domain_coloring.html">Source</a>. */
    private static Complex fn(Complex z) {
        return z.plus(2).pow(2)
                .times(z.minus(ONE_PLUS_2_I))
                .times(z.plusI(1));
    }
}
