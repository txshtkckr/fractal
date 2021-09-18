package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object ReciprocalZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("1/z")
        .computeFn(Complex::inverse)
        .render()
}