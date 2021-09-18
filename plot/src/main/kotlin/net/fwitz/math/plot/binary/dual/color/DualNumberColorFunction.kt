package net.fwitz.math.plot.binary.dual.color

import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot
import java.awt.Color
import java.util.function.BiFunction

@FunctionalInterface
interface DualNumberColorFunction : (DualNumber, DualNumber) -> Color {
    /**
     * Calculate the color for the given input and output values.
     *
     * @param c the input value
     * @param z the output value
     * @return the color to paint
     */
    override fun invoke(c: DualNumber, z: DualNumber): Color
}