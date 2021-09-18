package net.fwitz.math.main.binary.complex.functions.exponential

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object Exp1OverZSquared {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("exp(1/z^2)")
        .computeFn { z -> z.pow2.inverse.exp }
        .domainX(-1.0, 1.0)
        .domainY(-1.0, 1.0)
        .render()
}