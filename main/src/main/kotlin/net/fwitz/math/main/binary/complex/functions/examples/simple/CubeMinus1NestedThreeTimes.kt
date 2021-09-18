package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object CubeMinus1NestedThreeTimes {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("((z^3 - 1)^3 - 1)^3 - 1")
        .computeFn { z -> z.pow3.minus(1).pow3.minus(1).pow3.minus(1) }
        .domainX(0.5, 1.5)
        .domainY(-0.5, 0.5)
        .render()
}