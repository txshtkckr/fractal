package net.fwitz.math.main.binary.escape.orth.tchebu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev U2 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 2nd order U Tchebychev polynomial (`4z^2 - 1`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevU2 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*U2(z)  (Escape time)"
    override val pMin = -1.2
    override val pMax = 1.2
    override val qMin = -1.2
    override val qMax = 1.2

    override fun f(z: Complex) = z.pow2 * 4 - 1
    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}
