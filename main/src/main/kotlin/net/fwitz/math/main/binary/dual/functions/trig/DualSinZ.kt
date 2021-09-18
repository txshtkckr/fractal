package net.fwitz.math.main.binary.dual.functions.trig

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualSinZ {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("sin(z)")
        .computeFn { z -> z.sin }
        .render()
}