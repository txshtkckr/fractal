package net.fwitz.math.main.binary.dual.functions.trig

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualTanZ {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("tan(z)")
        .computeFn { z -> z.tan }
        .render()
}