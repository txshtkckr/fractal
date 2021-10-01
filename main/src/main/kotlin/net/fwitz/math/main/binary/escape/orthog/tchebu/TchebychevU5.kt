package net.fwitz.math.main.binary.escape.orthog.tchebu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev U5 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 5th order U Tchebychev polynomial (`32z^5 - 32z^3 + 6z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevU5 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*U5(z)  (Escape time)"
    override val pMin = -0.6
    override val pMax = 0.6
    override val qMin = -0.6
    override val qMax = 0.6

    override fun step(c: Complex, z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z5 = z2 * z3
        return c * (z5 * 32 - z3 * 32 + z * 6)
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}
