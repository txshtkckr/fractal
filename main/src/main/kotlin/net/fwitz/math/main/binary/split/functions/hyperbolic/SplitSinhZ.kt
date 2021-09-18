package net.fwitz.math.main.binary.split.functions.hyperbolic

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitSinhZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("sinh(z)")
        .computeFn(SplitComplex::sinh)
        .render()
}