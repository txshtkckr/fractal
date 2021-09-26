package net.fwitz.math.main.binary.escape.orth.tchebc

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev C6 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 6th order C Tchebychev polynomial (`z^6 - 6z^4 + 9z^2 - 2`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevC6 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*C6(z)  (Escape time)"
    override val pMin = -0.9
    override val pMax = 0.9
    override val qMin = -0.5
    override val qMax = 0.5

    override fun f(z: Complex): Complex {
        val z2 = z.pow2
        val z4 = z2.pow2
        val z6 = z2 * z4
        return z6 - z4 * 6 + z2 * 9 - 2
    }

    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}
