package net.fwitz.math.main.binary.complex.functions.trig

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object TanZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("tan(z)")
        .computeFn(Complex::tan)
        .render()
}