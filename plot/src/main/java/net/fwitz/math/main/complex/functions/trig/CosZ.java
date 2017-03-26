package net.fwitz.math.main.complex.functions.trig;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class CosZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("cos(z)")
                .computeFn(Complex::cos)
                .domainRe(-Math.PI, Math.PI)
                .domainIm(-Math.PI, Math.PI)
                .render();
    }
}
