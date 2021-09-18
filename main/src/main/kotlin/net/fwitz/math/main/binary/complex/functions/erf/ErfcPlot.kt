package net.fwitz.math.main.binary.complex.functions.erf

import net.fwitz.math.binary.complex.functions.Erf
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object ErfcPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("erfc(z)")
        .computeFn(Erf::erfc)
        .domainX(-5.0, 5.0)
        .domainY(-5.0, 5.0)
        .render()
}