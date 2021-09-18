package net.fwitz.math.main.binary.complex.functions.examples.hanlu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu

object HanluFig14 {
    private val ONE_MINUS_2_I = Complex(1, -2)
    private val TWO_PLUS_2_I = Complex(2, 2)

    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("(z−2)^2 (z+1−2i) (z+2+2i) / z^3")
        .computeFn(::fn)
        .colorFn(DomainColoringHanlu)
        .render()

    /**
     * [Source](http://users.mai.liu.se/hanlu09/complex/domain_coloring.html).
     */
    private fun fn(z: Complex) = (z - 2).pow2 *
            (z + ONE_MINUS_2_I) *
            (z + TWO_PLUS_2_I) *
            z.pow(-3)
}