package net.fwitz.math.main.binary.dual.functions.examples.simple

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualInverseInverse {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("1 / (1/z)")
        .computeFn { z -> z.inverse.inverse }
        .render()
}