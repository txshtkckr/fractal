package net.fwitz.math.main.binary.escape.orth.tchebs

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev S4 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 4th order S Tchebychev polynomial (`z^4 - 3z^2 + 1`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevS4 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*S4(z)  (Escape time)"
    override val pMin = -1.2
    override val pMax = 1.2
    override val qMin = -1.2
    override val qMax = 1.2

    override fun f(z: Complex): Complex {
        val z2 = z.pow2
        val z4 = z2.pow2
        return z4 - z2 * 3 + 1
    }

    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}
