package net.fwitz.math.main.binary.complex.functions.trig.inverse

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object SincZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("sinc(z) = sin(z)/z")
        .domainX(-10.0, 10.0)
        .domainY(-5.0, 5.0)
        .size(800, 400)
        .computeFn { z -> z.sin / z }
        .render()
}