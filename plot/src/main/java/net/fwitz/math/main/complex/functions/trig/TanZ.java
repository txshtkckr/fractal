package net.fwitz.math.main.complex.functions.trig;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class TanZ {
    public static void main(String[] args) {
        new FunctionPlot("tan(z)")
                .fn(Complex::tan)
                .render();
    }
}
