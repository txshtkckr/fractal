package net.fwitz.math.main.complex.functions.trig.inverse;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class AtanZ {
    public static void main(String[] args) {
        new FunctionPlot("atan(z)")
                .fn(Complex::atan)
                .render();
    }
}
