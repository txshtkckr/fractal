package net.fwitz.math.main.binary.escape.trig

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Sine Set". This is a Mandelbrot-style escape-time plot where the generating function is to take the
 * sine of the previous value rather than its square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Transcendental Functions" (pp. 128-129)
 */
object SinSetPlot: AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = sin(z(n-1)) + c  (Escape time)"
    override val pMin = -4.0
    override val pMax = 4.0
    override val qMin = -4.5
    override val qMax = 4.5

    override fun step(c: Complex, z: Complex) = z.sin + c

    @JvmStatic
    fun main(args: Array<String>) = render()
}