package net.fwitz.math.plot.binary.escape.color;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeTimeResult;

import java.awt.*;
import java.util.function.BiFunction;

@FunctionalInterface
public interface EscapeTimeColorFunction extends BiFunction<Complex, EscapeTimeResult, Color> {
    /**
     * Calculate the color for the given input value and escape time result.
     *
     * @param c      the input value
     * @param result the result of the escape time iteration
     * @return the color to paint
     */
    @Override
    Color apply(Complex c, EscapeTimeResult result);
}
