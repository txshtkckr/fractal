package net.fwitz.math.plot.binary.complex.color;

import net.fwitz.math.complex.Complex;

import java.awt.*;
import java.util.function.BiFunction;

@FunctionalInterface
public interface ComplexColorFunction extends BiFunction<Complex, Complex, Color> {
    /**
     * Calculate the color for the given input and output values.
     *
     * @param c the input value
     * @param z the output value
     * @return the color to paint
     */
    @Override
    Color apply(Complex c, Complex z);
}
