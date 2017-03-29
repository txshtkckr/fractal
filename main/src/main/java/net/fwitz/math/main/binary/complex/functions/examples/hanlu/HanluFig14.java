package net.fwitz.math.main.binary.complex.functions.examples.hanlu;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

import static net.fwitz.math.binary.complex.Complex.complex;

public class HanluFig14 {
    private static final Complex ONE_MINUS_2_I = complex(1, -2);
    private static final Complex TWO_PLUS_2_I = complex(2, 2);

    public static void main(String[] args) {
        new ComplexFunctionPlot("(z−2)^2 (z+1−2i) (z+2+2i) / z^3")
                .computeFn(HanluFig14::fn)
                .colorFn(new DomainColoringHanlu())
                .render();
    }

    /**
     * <a href="http://users.mai.liu.se/hanlu09/complex/domain_coloring.html">Source</a>.
     */
    private static Complex fn(Complex z) {
        Complex c1 = z.minus(2).pow(2);
        Complex c2 = z.plus(ONE_MINUS_2_I);
        Complex c3 = z.plus(TWO_PLUS_2_I);
        Complex c4 = z.pow(-3);
        return c1.times(c2).times(c3).times(c4);
    }
}
