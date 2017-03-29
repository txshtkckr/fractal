package net.fwitz.math.main.binary.complex.functions.exponential;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class Exp1OverZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("exp(1/z)")
                .computeFn(z -> z.inverse().exp())
                .domainX(-1, 1)
                .domainY(-1, 1)
                .render();
    }
}
