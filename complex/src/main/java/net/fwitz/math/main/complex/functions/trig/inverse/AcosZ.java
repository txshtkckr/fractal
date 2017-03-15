package net.fwitz.math.main.complex.functions.trig.inverse;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class AcosZ {
    public static void main(String[] args) {
        new FunctionPlot("acos(z)")
                .fn(Complex::acos)
                .render();
    }
}
