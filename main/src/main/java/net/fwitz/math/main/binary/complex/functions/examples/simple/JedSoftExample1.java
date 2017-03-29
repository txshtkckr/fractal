package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

import static java.lang.Math.PI;

// http://www.jedsoft.org/fun/complex/
public class JedSoftExample1 {
    public static final String FN_NAME = "f(z) = [z + z^2 / sin(z^4 - 1)]^2";

    public static void main(String[] args) {
        new ComplexFunctionPlot(FN_NAME)
                .domainX(-PI, PI)
                .domainY(-PI, PI)
                .computeFn(JedSoftExample1::fn)
                .render();
    }

    public static Complex fn(Complex z) {
        Complex z2 = z.times(z);
        Complex z4 = z2.times(z2);
        Complex frac = z2.div(z4.minus(1).sin());
        Complex brackets = z.plus(frac);
        return brackets.times(brackets);
    }
}
