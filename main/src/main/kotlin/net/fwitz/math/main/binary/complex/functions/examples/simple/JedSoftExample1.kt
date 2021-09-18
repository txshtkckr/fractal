package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

// http://www.jedsoft.org/fun/complex/
object JedSoftExample1 {
    const val FN_NAME = "f(z) = [z + z^2 / sin(z^4 - 1)]^2"

    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot(FN_NAME)
        .domainX(-Math.PI, Math.PI)
        .domainY(-Math.PI, Math.PI)
        .computeFn(::fn)
        .render()

    fun fn(z: Complex): Complex {
        val z2 = z.pow2
        val z4 = z2.pow2
        val brackets = z + z2 / (z4 - 1).sin
        return brackets.pow2
    }
}