package net.fwitz.math.main.binary.split.functions.trig

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitSinZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("sin(z)")
        .computeFn { z -> z.sin }
        .render()
}