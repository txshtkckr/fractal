package net.fwitz.math.main.binary.escape.orthog.tchebu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev U7 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 7th order U Tchebychev polynomial (`128z^7 - 192z^5 + 80z^3 - 8z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevU7 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*U7(z)  (Escape time)"
    override val pMin = -0.6
    override val pMax = 0.6
    override val qMin = -0.6
    override val qMax = 0.6

    override fun step(c: Complex, z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z5 = z2 * z3
        val z7 = z2 * z5
        return c * (z7 * 128 - z5 * 192 + z3 * 80 - z * 8)
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}
