package net.fwitz.math.main.binary.split.functions.examples.simple

import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitIdentityMain {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("z")
        .computeFn { z -> z }
        .render()
}