package net.fwitz.math.main.binary.complex.functions.erf

import net.fwitz.math.binary.complex.functions.Erf
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringSmooth

object ErfPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("erf(z)")
        .computeFn(Erf::erf)
        .domainX(-5.0, 5.0)
        .domainY(-5.0, 5.0)
        .colorFn(DomainColoringSmooth)
        .render()
}