package net.fwitz.math.main.complex.functions.examples.simple;

import net.fwitz.math.plot.complex.ComplexFunctionPlot;

public class ExpZPowMinusOneHalf {
    public static void main(String[] args) {
        new ComplexFunctionPlot("exp(1 / z^(1/2))")
                .computeFn(z -> z.pow(0.5).inverse().exp())
                .render();
    }
}
