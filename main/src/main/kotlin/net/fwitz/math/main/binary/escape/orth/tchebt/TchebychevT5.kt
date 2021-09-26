package net.fwitz.math.main.binary.escape.orth.tchebt

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev T5 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 5th order T Tchebychev polynomial (`16z^5 - 20z^3 + 5z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevT5 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*T5(z)  (Escape time)"
    override val pMin = -1.2
    override val pMax = 1.2
    override val qMin = -1.2
    override val qMax = 1.2

    override fun f(z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z5 = z2 * z3
        return z5 * 16 - z3 * 20 + z * 5
    }

    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}