package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringContour

object IdentityMain {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("z")
        .computeFn { z -> z }
        .colorFn(DomainColoringContour)
        .render()
}