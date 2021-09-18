package net.fwitz.math.main.binary.split.functions.examples.simple

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitInverseZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("1/z")
        .computeFn { z -> z.inverse }
        .render()
}