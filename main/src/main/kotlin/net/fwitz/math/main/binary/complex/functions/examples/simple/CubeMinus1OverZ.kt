package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringContour

object CubeMinus1OverZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("(z^3 - 1) / z")
        .computeFn { z -> (z.pow3 - 1) / z }
        .domainX(-2.0, 2.0)
        .domainY(-2.0, 2.0)
        .colorFn(DomainColoringContour)
        .render()
}