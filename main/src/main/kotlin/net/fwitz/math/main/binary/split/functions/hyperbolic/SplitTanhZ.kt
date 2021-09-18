package net.fwitz.math.main.binary.split.functions.hyperbolic

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitTanhZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("tanh(z)")
        .computeFn(SplitComplex::tanh)
        .render()
}