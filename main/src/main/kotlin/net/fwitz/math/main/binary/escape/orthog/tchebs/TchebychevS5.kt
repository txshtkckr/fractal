package net.fwitz.math.main.binary.escape.orthog.tchebs

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev S5 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 5th order S Tchebychev polynomial (`z^5 - 4z^3 + 3z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevS5 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*S5(z)  (Escape time)"
    override val pMin = -0.9
    override val pMax = 0.9
    override val qMin = -0.9
    override val qMax = 0.9

    override fun step(c: Complex, z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z5 = z2 * z3
        return c * (z5 - z3 * 4 + z * 3)
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}
