package net.fwitz.math.main.binary.complex.functions.trig;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class SinZ2 {
    public static void main(String[] args) {
        new ComplexFunctionPlot("sin(z^2)")
                .computeFn(z -> z.times(z).sin())
                .domainX(-Math.PI, Math.PI)
                .domainY(-Math.PI, Math.PI)
                .render();
    }
}
