package net.fwitz.math.main.binary.complex.functions.exponential

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object ExpZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("exp(z)")
        .computeFn(Complex::exp)
        .render()
}