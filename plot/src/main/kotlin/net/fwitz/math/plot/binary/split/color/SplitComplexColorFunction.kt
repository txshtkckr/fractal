package net.fwitz.math.plot.binary.split.color

import net.fwitz.math.binary.split.SplitComplex
import java.awt.Color

@FunctionalInterface
interface SplitComplexColorFunction : (SplitComplex, SplitComplex) -> Color {
    /**
     * Calculate the color for the given input and output values.
     *
     * @param c the input value
     * @param z the output value
     * @return the color to paint
     */
    override fun invoke(c: SplitComplex, z: SplitComplex): Color
}
