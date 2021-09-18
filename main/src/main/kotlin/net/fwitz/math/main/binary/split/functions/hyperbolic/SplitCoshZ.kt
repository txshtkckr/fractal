package net.fwitz.math.main.binary.split.functions.hyperbolic

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitCoshZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("cosh(z)")
        .computeFn(SplitComplex::cosh)
        .render()
}