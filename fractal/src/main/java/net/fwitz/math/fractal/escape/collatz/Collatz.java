package net.fwitz.math.fractal.escape.collatz;

import net.fwitz.math.binary.complex.Complex;

public class Collatz {
    public static Complex collatz(Complex z) {
        Complex onePlus4z = z.times(4).plus(1);
        Complex onePlus2z = z.times(2).plus(1);
        Complex cosPiZ = z.times(Math.PI).cos();
        return onePlus4z.minus(onePlus2z.times(cosPiZ)).times(0.25);
    }
}
