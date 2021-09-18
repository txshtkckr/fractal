package net.fwitz.math.main.binary.split.functions.trig

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitCosZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("cos(z)")
        .computeFn { z -> z.cos }
        .render()
}