package net.fwitz.math.main.binary.escape.orthog.tchebt

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev T2 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 2nd order T Tchebychev polynomial (`2z^2 - 1`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevT2 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*T2(z)  (Escape time)"
    override val pMin = -1.2
    override val pMax = 1.2
    override val qMin = -1.2
    override val qMax = 1.2

    override fun step(c: Complex, z: Complex) = c * (z.pow2 * 2 - 1)

    @JvmStatic
    fun main(args: Array<String>) = render()
}
