package net.fwitz.math.main.binary.complex.functions.hyperbolic

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object TanhZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("tanh(z)")
        .computeFn(Complex::tanh)
        .render()
}