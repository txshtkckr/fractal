package net.fwitz.math.main.binary.split.functions.examples.simple

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitZ2 {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("z^2")
        .computeFn(SplitComplex::pow2)
        .render()
}