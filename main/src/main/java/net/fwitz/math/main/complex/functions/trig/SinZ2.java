package net.fwitz.math.main.complex.functions.trig;

import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class SinZ2 {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sin(z^2)")
                .computeFn(z -> z.times(z).sin())
                .domainRe(-Math.PI, Math.PI)
                .domainIm(-Math.PI, Math.PI)
                .render();
    }
}
