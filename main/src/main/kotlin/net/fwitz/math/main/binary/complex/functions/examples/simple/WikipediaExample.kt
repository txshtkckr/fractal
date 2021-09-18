package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringSmooth

/**
 * The domain coloring example on Wikipedia uses this function as an example.
 *
 *
 * (*z*<sup>2</sup> − 1)(*z* − 2 − *i*)<sup>2</sup> /
 * *z*<sup>2</sup> + 2 + 2*i*.
 */
object WikipediaExample {
    const val FN_NAME = "(z^2 - 1)(z - 2 - i)^2 / (z^2 + 2 + 2i)"

    private val TWO_PLUS_I = Complex(2, 1)
    private val TWO_PLUS_TWO_I = Complex(2, 2)

    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot(FN_NAME)
        .computeFn(::fn)
        .colorFn(DomainColoringSmooth)
        .render()

    fun fn(z: Complex) = (z * 2 - 1) * (z - TWO_PLUS_I).pow2 /
            (z.pow2 + TWO_PLUS_TWO_I)
}