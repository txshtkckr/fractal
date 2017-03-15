package net.fwitz.math.main.complex.functions.exponential;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;

public class LogZ {
    public static void main(String[] args) {
        new FunctionPlot("log(z)")
                .fn(Complex::log)
                .render();
    }
}
