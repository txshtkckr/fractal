package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

import static java.lang.Math.PI;

// http://www.jedsoft.org/fun/complex/
public class JedSoftExample2 {
    public static final String FN_NAME = "f(z) = cos(z) / sin(z^4 - 1)";

    public static void main(String[] args) {
        new ComplexFunctionPlot(FN_NAME)
                .domainRe(-PI, PI)
                .domainIm(-PI, PI)
                .computeFn(JedSoftExample2::fn)
                .render();
    }

    public static Complex fn(Complex z) {
        return z.cos().div(z.pow(4).minus(1).sin());
    }
}
