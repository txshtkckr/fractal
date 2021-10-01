package net.fwitz.math.main.binary.escape.orthog.tchebs

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev S6 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 6th order S Tchebychev polynomial (`z^6 - 5z^4 + 6z^2 - 1`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevS6 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*S6(z)  (Escape time)"
    override val pMin = -1.0
    override val pMax = 1.0
    override val qMin = -1.0
    override val qMax = 1.0

    override fun step(c: Complex, z: Complex): Complex {
        val z2 = z.pow2
        val z4 = z2.pow2
        val z6 = z2 * z4
        return c * (z6 - z4 * 5 + z2 * 6 - 1)
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}
