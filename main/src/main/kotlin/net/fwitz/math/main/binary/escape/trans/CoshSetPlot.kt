package net.fwitz.math.main.binary.escape.trans

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractIteratedFunctionMapPlot

/**
 * The "Cosh Set". This is a Mandelbrot-style escape-time plot where the generating function is to take the
 * hyperbolic cosine of the previous value rather than its square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Transcendental Functions" (pp. 131-133)
 */
object CoshSetPlot : AbstractIteratedFunctionMapPlot() {
    override val title: String = "z(n) = cosh(z(n-1)) + c  (Escape time)"
    override val pMin = -4.5
    override val pMax = 4.5
    override val qMin = -4.5
    override val qMax = 4.5

    override fun f(z: Complex) = z.cosh

    @JvmStatic
    fun main(args: Array<String>) = render()
}