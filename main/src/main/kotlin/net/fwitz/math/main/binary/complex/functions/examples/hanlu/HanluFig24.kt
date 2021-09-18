package net.fwitz.math.main.binary.complex.functions.examples.hanlu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu

object HanluFig24 {
    private val ONE_PLUS_I = Complex(1, 1)

    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("(1 + i)sin(z)")
        .computeFn(::iter)
        .colorFn(DomainColoringHanlu)
        .render()

    /**
     * [Source](http://users.mai.liu.se/hanlu09/complex/domain_coloring.html).
     */
    private fun iter(z0: Complex): Complex {
        var z = z0
        for (i in 1..5) z = fn(z)
        return z
    }

    private fun fn(z: Complex) = z.sin * ONE_PLUS_I
}