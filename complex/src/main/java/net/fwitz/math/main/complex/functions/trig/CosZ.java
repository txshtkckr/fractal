package net.fwitz.math.main.complex.functions.trig;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class CosZ {
    public static void main(String[] args) {
        new FunctionPlot("cos(z)")
                .fn(Complex::cos)
                .render();
    }
}
