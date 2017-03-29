package net.fwitz.math.main.binary.complex.functions.exponential;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class LogZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("log(z)")
                .computeFn(Complex::log)
                .render();
    }
}
