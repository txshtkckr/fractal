package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvancedPlusLogScale

object MultiplePole {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("z ^ -3")
        .computeFn { z -> z.pow(-3.0) }
        .colorFn(DomainColoringAdvancedPlusLogScale)
        .domainX(-1.5, 1.5)
        .domainY(-1.5, 1.5)
        .render()
}