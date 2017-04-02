package net.fwitz.math.main.binary.complex.functions.examples.simple;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class ReciprocalZ {
    public static void main(String[] args) {
        new ComplexFunctionPlot("1/z")
                .computeFn(Complex::inverse)
                .render();
    }
}