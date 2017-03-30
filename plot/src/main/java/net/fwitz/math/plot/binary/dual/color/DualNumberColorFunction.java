package net.fwitz.math.plot.binary.dual.color;

import net.fwitz.math.binary.dual.DualNumber;

import java.awt.*;
import java.util.function.BiFunction;

@FunctionalInterface
public interface DualNumberColorFunction extends BiFunction<DualNumber, DualNumber, Color> {
    /**
     * Calculate the color for the given input and output values.
     *
     * @param c the input value
     * @param z the output value
     * @return the color to paint
     */
    @Override
    Color apply(DualNumber c, DualNumber z);
}
