package net.fwitz.math.main.binary.dual.functions.exponential

import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualExp1OverZ {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("exp(1/z)")
        .computeFn { z: DualNumber -> z.inverse.exp }
        .render()
}