package net.fwitz.math.main.binary.split.functions.examples.simple

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitTimes2PlusJ {
    val TWO_PLUS_J = SplitComplex(2, 1)

    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("f(z) = z * (2 + j)")
        .computeFn { z -> z * TWO_PLUS_J }
        .render()
}