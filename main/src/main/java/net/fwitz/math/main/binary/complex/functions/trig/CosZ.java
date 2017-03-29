package net.fwitz.math.main.binary.complex.functions.trig;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class CosZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("cos(z)")
                .computeFn(Complex::cos)
                .domainX(-Math.PI, Math.PI)
                .domainY(-Math.PI, Math.PI)
                .render();
    }
}
