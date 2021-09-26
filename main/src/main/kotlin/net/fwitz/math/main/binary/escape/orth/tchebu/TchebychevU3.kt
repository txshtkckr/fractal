package net.fwitz.math.main.binary.escape.orth.tchebu

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Tchebychev U3 Set". This is a Mandelbrot-style escape-time plot where the generating function is to
 * multiply the 3rd order U Tchebychev polynomial (`8z^3 - 4z`) of the previous value by `c`
 * instead of adding `c` to the previous value's square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Orthogonal Polynomials" (pp. 138-142)
 */
object TchebychevU3 : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = c*U3(z)  (Escape time)"
    override val pMin = -0.9
    override val pMax = 0.9
    override val qMin = -0.9
    override val qMax = 0.9

    override fun f(z: Complex) = z.pow3 * 8 - z * 4
    override fun op(fz: Complex, c: Complex) = c * fz

    @JvmStatic
    fun main(args: Array<String>) = render()
}
