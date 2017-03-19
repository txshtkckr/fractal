package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.plot.FunctionPlot;

public class ExpZPowMinusOneHalf {
    public static void main(String[] args) {
        new FunctionPlot("exp(1 / z^(1/2))")
                .fn(z -> z.pow(0.5).inverse().exp())
                .render();
    }
}
