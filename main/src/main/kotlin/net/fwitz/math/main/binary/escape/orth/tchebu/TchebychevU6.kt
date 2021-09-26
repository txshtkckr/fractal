package net.fwitz.math.main.binary.escape.orth.tchebu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev U6 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 6th order U Tchebychev polynomial (64z^6 - 80z^4 + 24z^2 - 1`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevU6 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*U6(z)  (Escape time)"
    override val pMin = -0.9
    override val pMax = 0.9
    override val qMin = -0.9
    override val qMax = 0.9

    override fun f(z: Complex): Complex {
        val z2 = z.pow2
        val z4 = z2.pow2
        val z6 = z2 * z4
        return z6 * 64 - z4 * 80 + z2 * 24 - 1
    }

    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}
