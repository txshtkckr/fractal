package net.fwitz.math.main.complex.functions.trig.inverse;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class AsinZ {
    public static void main(String[] args) {
        new FunctionPlot("asin(z)")
                .fn(Complex::asin)
                .render();
    }
}
