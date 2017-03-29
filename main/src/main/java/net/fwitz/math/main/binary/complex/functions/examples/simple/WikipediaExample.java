package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.color.DomainColoringSmooth;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

import static net.fwitz.math.binary.complex.Complex.complex;

/**
 * The domain coloring example on Wikipedia uses this function as an example.
 * <p>
 * (<i>z</i><sup>2</sup> − 1)(<i>z</i> − 2 − <i>i</i>)<sup>2</sup> /
 * <i>z</i><sup>2</sup> + 2 + 2<i>i</i>.
 */
public class WikipediaExample {
    public static final String FN_NAME = "(z^2 - 1)(z - 2 - i)^2 / (z^2 + 2 + 2i)";
    private static final Complex TWO_PLUS_I = complex(2, 1);
    private static final Complex TWO_PLUS_TWO_I = complex(2, 2);

    public static void main(String[] args) {
        new ComplexFunctionPlot(FN_NAME)
                .computeFn(WikipediaExample::fn)
                .colorFn(new DomainColoringSmooth())
                .render();
    }

    public static Complex fn(Complex z) {
        Complex z2m1 = z.times(z).minus(1);
        Complex zm2mi = z.minus(TWO_PLUS_I);
        Complex z2p2p2i = z.times(z).plus(TWO_PLUS_TWO_I);
        return z2m1.times(zm2mi).times(zm2mi).div(z2p2p2i);
    }
}
