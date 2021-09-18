package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color

@FunctionalInterface
interface ComplexColorFunction : (Complex, Complex) -> Color {
    /**
     * Calculate the color for the given input and output values.
     *
     * @param c the input value
     * @param z the output value
     * @return the color to paint
     */
    override fun invoke(c: Complex, z: Complex): Color
}