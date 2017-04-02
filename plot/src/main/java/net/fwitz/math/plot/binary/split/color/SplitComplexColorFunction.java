package net.fwitz.math.plot.binary.split.color;

import net.fwitz.math.binary.split.SplitComplex;

import java.awt.*;
import java.util.function.BiFunction;

@FunctionalInterface
public interface SplitComplexColorFunction extends BiFunction<SplitComplex, SplitComplex, Color> {
    /**
     * Calculate the color for the given input and output values.
     *
     * @param c the input value
     * @param z the output value
     * @return the color to paint
     */
    @Override
    Color apply(SplitComplex c, SplitComplex z);
}