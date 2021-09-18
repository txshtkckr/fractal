package net.fwitz.math.main.binary.complex.functions.erf

import net.fwitz.math.binary.complex.functions.Erf
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object ErfiPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("erfi(z)")
        .computeFn(Erf::erfi)
        .domainX(-5.0, 5.0)
        .domainY(-5.0, 5.0)
        .render()
}