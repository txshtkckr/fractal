package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object SinReciprocalZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("sin(1/z)")
        .computeFn { z -> z.inverse.sin }
        .render()
}