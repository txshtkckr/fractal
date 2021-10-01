package net.fwitz.math.main.binary.escape.orthog.tchebt

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev T7 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 7th order T Tchebychev polynomial (`64z^7 - 112z^5 + 56z^3 - 7z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevT7 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*T7(z)  (Escape time)"
    override val pMin = -1.0
    override val pMax = 1.0
    override val qMin = -1.0
    override val qMax = 1.0

    override fun step(c: Complex, z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z5 = z2 * z3
        val z7 = z2 * z5
        return c * (z7 * 64 - z5 * 112 + z3 * 56 - z * 7)
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}
