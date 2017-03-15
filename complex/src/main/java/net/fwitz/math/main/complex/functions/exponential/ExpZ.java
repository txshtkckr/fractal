package net.fwitz.math.main.complex.functions.exponential;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class ExpZ {
    public static void main(String[] args) {
        new FunctionPlot("exp(z)")
                .fn(Complex::exp)
                .render();
    }
}
