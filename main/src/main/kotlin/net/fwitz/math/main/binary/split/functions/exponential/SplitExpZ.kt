package net.fwitz.math.main.binary.split.functions.exponential

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitExpZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("exp(z)")
        .computeFn(SplitComplex::exp)
        .render()
}