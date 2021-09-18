package net.fwitz.math.main.binary.split.functions.exponential

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitLogZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("log(z)")
        .computeFn(SplitComplex::log)
        .render()
}