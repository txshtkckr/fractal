package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

import static java.lang.Math.PI;

// http://www.jedsoft.org/fun/complex/
public class JedSoftExample2 {
    public static void main(String[] args) {
        new ComplexFunctionPlot("f(z) = cos(z) / sin(z^4 - 1)")
                .domainRe(-PI, PI)
                .domainIm(-PI, PI)
                .computeFn(JedSoftExample2::fn)
                .render();
    }

    private static Complex fn(Complex z) {
        return z.cos().div(z.pow(4).minus(1).sin());
    }
}
