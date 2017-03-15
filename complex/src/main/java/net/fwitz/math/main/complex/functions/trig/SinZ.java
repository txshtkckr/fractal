package net.fwitz.math.main.complex.functions.trig;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class SinZ {
    public static void main(String[] args) {
        new FunctionPlot("sin(z)")
                .fn(Complex::sin)
                .domainRe(-Math.PI, Math.PI)
                .domainIm(-Math.PI, Math.PI)
                .render();
    }
}
