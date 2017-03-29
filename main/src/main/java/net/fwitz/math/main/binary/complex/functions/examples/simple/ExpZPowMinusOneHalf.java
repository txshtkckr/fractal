package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class ExpZPowMinusOneHalf {
    public static void main(String[] args) {
        new ComplexFunctionPlot("exp(1 / z^(1/2))")
                .computeFn(z -> z.pow(0.5).inverse().exp())
                .render();
    }
}
