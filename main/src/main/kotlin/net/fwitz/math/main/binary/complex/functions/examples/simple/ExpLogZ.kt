package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvanced

object ExpLogZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("e^(log z)")
        .computeFn { z -> z.log.exp }
        .colorFn(DomainColoringAdvanced())
        .render()
}