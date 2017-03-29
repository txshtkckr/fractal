package net.fwitz.math.main.binary.complex.functions.trig;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class TanZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("tan(z)")
                .computeFn(Complex::tan)
                .render();
    }
}
