package net.fwitz.math.main.binary.escape.orth.tchebc

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev C5 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 5th order C Tchebychev polynomial (`z^5 - 5z^3 + 5z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevC5 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*C5(z)  (Escape time)"
    override val pMin = -0.9
    override val pMax = 0.9
    override val qMin = -0.9
    override val qMax = 0.9

    override fun f(z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z5 = z2 * z3
        return z5 - z3 * 5 + z * 5
    }

    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}
