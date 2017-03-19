package net.fwitz.math.main.complex.functions.exponential;

import net.fwitz.math.plot.FunctionPlot;

public class Exp1OverZ {
    public static void main(String[] args) {
        new FunctionPlot("exp(1/z)")
                .fn(z -> z.inverse().exp())
                .domainRe(-1, 1)
                .domainIm(-1, 1)
                .render();
    }
}
