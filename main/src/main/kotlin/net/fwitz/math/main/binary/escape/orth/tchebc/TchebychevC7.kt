package net.fwitz.math.main.binary.escape.orth.tchebc

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev C7 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 7th order C Tchebychev polynomial (`z^7 - 7z^5 + 14z^3 - 7z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevC7 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*C7(z)  (Escape time)"
    override val pMin = -1.0
    override val pMax = 1.0
    override val qMin = -1.0
    override val qMax = 1.0

    override fun f(z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z5 = z2 * z3
        val z7 = z2 * z5
        return z7 - z5 * 7 + z3 * 14 - z * 7
    }

    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}
