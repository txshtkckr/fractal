package net.fwitz.math.main.binary.escape.orthog.tchebt

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Tchebychev T6 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 6th order T Tchebychev polynomial (`32z^6 - 48z^4 + 18z^2 - 1`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevT6 : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = c*T6(z)  (Escape time)"
    override val pMin = -0.9
    override val pMax = 0.9
    override val qMin = -0.9
    override val qMax = 0.9

    override fun step(c: Complex, z: Complex): Complex {
        val z2 = z.pow2
        val z4 = z2.pow2
        val z6 = z2 * z4
        return c * (z6 * 32 - z4 * 48 + z2 * 18 - 1)
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}