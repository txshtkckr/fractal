package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

// http://www.jedsoft.org/fun/complex/
object JedSoftExample2 {
    const val FN_NAME = "f(z) = cos(z) / sin(z^4 - 1)"

    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot(FN_NAME)
        .domainX(-Math.PI, Math.PI)
        .domainY(-Math.PI, Math.PI)
        .computeFn(::fn)
        .render()

    fun fn(z: Complex) = z.cos / (z.pow(4) - 1).sin
}