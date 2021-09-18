package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object ExpZPowMinusOneHalf {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("exp(1 / z^(1/2))")
        .computeFn { z -> z.sqrt.inverse.exp }
        .render()
}