package net.fwitz.math.main.binary.dual.functions.examples.simple

import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualIdentityMain {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("z")
        .computeFn { z -> z }
        .render()
}