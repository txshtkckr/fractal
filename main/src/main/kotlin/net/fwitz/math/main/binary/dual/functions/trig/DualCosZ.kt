package net.fwitz.math.main.binary.dual.functions.trig

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualCosZ {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("cos(z)")
        .computeFn { z -> z.cos }
        .render()
}