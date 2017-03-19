package net.fwitz.math.main.complex.functions.trig.inverse;

import net.fwitz.math.plot.FunctionPlot;

public class SincZ {
    public static void main(String[] args) {
        new FunctionPlot("sinc(z) = sin(z)/z")
                .domainRe(-10, 10)
                .domainIm(-5, 5)
                .size(800, 400)
                .fn(z -> z.sin().div(z))
                .render();
    }
}
