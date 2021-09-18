package net.fwitz.math.main.binary.complex.functions.hyperbolic

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object SinhZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("sinh(z)")
        .computeFn(Complex::sinh)
        .render()
}