package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvanced

object ZexpZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("z^z")
        .computeFn { z -> z.pow(z) }
        .domainX(-2.0, 2.0)
        .domainY(-2.0, 2.0)
        .colorFn(DomainColoringAdvanced())
        .render()
}