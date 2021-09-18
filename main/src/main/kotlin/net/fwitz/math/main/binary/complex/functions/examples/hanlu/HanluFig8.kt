package net.fwitz.math.main.binary.complex.functions.examples.hanlu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu
import java.util.function.Function

object HanluFig8 {
    private val ONE_PLUS_2_I = Complex(1, 2)

    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("(z+2)^2 (z−1−2i) (z+i)")
        .computeFn(::fn)
        .colorFn(DomainColoringHanlu)
        .render()

    /**
     * [Source](http://users.mai.liu.se/hanlu09/complex/domain_coloring.html).
     */
    private fun fn(z: Complex) = (z + 2).pow2 * (z - ONE_PLUS_2_I) * (z.plusY(1))
}