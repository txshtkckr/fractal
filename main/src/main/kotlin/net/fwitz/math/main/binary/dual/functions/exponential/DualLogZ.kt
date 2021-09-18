package net.fwitz.math.main.binary.dual.functions.exponential

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualLogZ {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("log(z)")
        .computeFn { z -> z.log }
        .render()
}