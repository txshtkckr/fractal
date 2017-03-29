package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class Z8p15Z4m16 {
    public static final String FN_NAME = "f(z) = z^8 + 15z^4 - 16";

    public static void main(String[] args) {
        new ComplexFunctionPlot(FN_NAME)
                .domainX(-3.5, 3.5)
                .domainX(-2.5, 2.5)
                .size(700, 500)
                .computeFn(Z8p15Z4m16::f)
                .render();
    }

    public static Complex f(Complex z) {
        return z.pow(8)
                .plus(z.pow(4).times(15))
                .minus(16);
    }

    public static Complex df(Complex z) {
        return z.pow(7).times(8)
                .plus(z.pow3().times(60));
    }

    public static Complex d2f(Complex z) {
        return z.pow(6).times(56)
                .plus(z.pow2().times(180));
    }
}
