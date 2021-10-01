package net.fwitz.math.main.binary.escape.orthog.tchebt

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev T4 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 4th order T Tchebychev polynomial (`8z^4 - 8z^2 + 1`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevT4 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*T4(z)  (Escape time)"
    override val pMin = -1.2
    override val pMax = 1.2
    override val qMin = -1.2
    override val qMax = 1.2

    override fun step(c: Complex, z: Complex): Complex {
        val z2 = z.pow2
        val z4 = z2.pow2
        return z4 * 8 - z2 * 8 + 1
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}