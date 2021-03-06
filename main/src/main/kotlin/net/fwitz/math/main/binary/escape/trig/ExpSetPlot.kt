package net.fwitz.math.main.binary.escape.trig

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Exp Set". This is a Mandelbrot-style escape-time plot where the generating function is to take the
 * complex exponential of the previous value rather than its square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Transcendental Functions" (pp. 129-131)
 */
object ExpSetPlot: AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = exp(z(n-1)) + c  (Escape time)"
    override val pMin = -2.0
    override val pMax = 3.0
    override val qMin = -5.0
    override val qMax = 5.0

    override fun step(c: Complex, z: Complex) = z.exp + c

    @JvmStatic
    fun main(args: Array<String>) = render()
}