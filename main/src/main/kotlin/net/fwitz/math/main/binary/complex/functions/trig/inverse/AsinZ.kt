package net.fwitz.math.main.binary.complex.functions.trig.inverse

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object AsinZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("asin(z)")
        .computeFn(Complex::asin)
        .render()
}