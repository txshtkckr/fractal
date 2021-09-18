package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object CubeMinus1 {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("z^3 - 1")
        .computeFn { z -> z.pow3 - 1 }
        .domainX(-1.5, 1.5)
        .domainY(-1.5, 1.5)
        .render()
}