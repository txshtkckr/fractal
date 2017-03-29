package net.fwitz.math.main.complex.functions.trig.inverse;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class SincZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sinc(z) = sin(z)/z")
                .domainX(-10, 10)
                .domainY(-5, 5)
                .size(800, 400)
                .computeFn(z -> z.sin().div(z))
                .render();
    }
}
