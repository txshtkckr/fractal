package net.fwitz.math.main.binary.complex.functions.exponential

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object Exp1OverZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("exp(1/z)")
        .computeFn { z -> z.inverse.exp }
        .domainX(-1.0, 1.0)
        .domainY(-1.0, 1.0)
        .render()
}