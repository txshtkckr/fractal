package net.fwitz.math.main.binary.split.functions.examples.simple

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitExpLogZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("e^(log z)")
        .computeFn { w -> w.region1mapped { z -> z.log.exp } }
        .render()
}