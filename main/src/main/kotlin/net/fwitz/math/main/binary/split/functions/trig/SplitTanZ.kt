package net.fwitz.math.main.binary.split.functions.trig

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.plot.binary.split.SplitComplexFunctionPlot

object SplitTanZ {
    @JvmStatic
    fun main(args: Array<String>) = SplitComplexFunctionPlot("tan(z)")
        .computeFn(SplitComplex::tan)
        .render()
}