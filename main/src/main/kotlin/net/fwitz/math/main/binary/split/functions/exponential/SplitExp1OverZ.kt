package net.fwitz.math.main.binary.split.functions.exponential

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitExp1OverZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("exp(1/z)")
        .computeFn { z -> z.inverse.exp }
        .render()
}