package net.fwitz.math.main.binary.escape.orthog.tchebt

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev T3 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 3rd order T Tchebychev polynomial (`4z^3 - 3z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevT3 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*T3(z)  (Escape time)"
    override val pMin = -1.2
    override val pMax = 1.2
    override val qMin = -1.2
    override val qMax = 1.2

    override fun step(c: Complex, z: Complex) = c * (z.pow3 * 4 - z * 3)

    @JvmStatic
    fun main(args: Array<String>) = render()
}
