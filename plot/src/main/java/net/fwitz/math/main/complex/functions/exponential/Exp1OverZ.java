package net.fwitz.math.main.complex.functions.exponential;

import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class Exp1OverZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("exp(1/z)")
                .computeFn(z -> z.inverse().exp())
                .domainRe(-1, 1)
                .domainIm(-1, 1)
                .render();
    }
}
