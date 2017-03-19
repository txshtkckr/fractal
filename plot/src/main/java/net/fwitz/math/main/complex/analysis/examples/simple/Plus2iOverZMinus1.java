package net.fwitz.math.main.complex.analysis.examples.simple;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.FunctionPlot;
import net.fwitz.math.plot.color.domain.DomainColoringAdvanced;

/**
 * Taken from the same StackOverflow answer that provided the {@link DomainColoringAdvanced} color function.
 */
public class Plus2iOverZMinus1 {
    public static void main(String[] args) {
        new FunctionPlot("(z + 2i) / (z - 1)")
                .fn(Plus2iOverZMinus1::fn)
                .render();
    }

    private static Complex fn(Complex z) {
        return z.plusI(2).div(z.minus(1));
    }
}
