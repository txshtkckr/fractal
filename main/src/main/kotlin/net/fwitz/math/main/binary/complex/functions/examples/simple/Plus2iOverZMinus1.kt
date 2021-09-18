package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

/**
 * Taken from the same StackOverflow answer that provided the [DomainColoringAdvanced] color function.
 */
object Plus2iOverZMinus1 {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("(z + 2i) / (z - 1)")
        .computeFn { z -> z.plusY(2) / (z - 1) }
        .render()
}