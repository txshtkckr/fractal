package net.fwitz.math.main.binary.complex.functions.trig

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object SinZ2 {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("sin(z^2)")
        .computeFn { z -> z.pow2.sin }
        .domainX(-Math.PI, Math.PI)
        .domainY(-Math.PI, Math.PI)
        .render()
}