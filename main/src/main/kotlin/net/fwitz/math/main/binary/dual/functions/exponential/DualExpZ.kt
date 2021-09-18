package net.fwitz.math.main.binary.dual.functions.exponential

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualExpZ {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("exp(z)")
        .computeFn { z -> z.exp }
        .render()
}