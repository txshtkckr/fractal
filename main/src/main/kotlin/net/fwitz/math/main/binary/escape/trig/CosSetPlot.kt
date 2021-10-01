package net.fwitz.math.main.binary.escape.trig

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The "Cosine Set". This is a Mandelbrot-style escape-time plot where the generating function is to take the
 * cosine of the previous value rather than its square.
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Transcendental Functions" (pp. 126-128)
 */
object CosSetPlot : AbstractEscapeTimeMapPlot() {
    override val title: String = "z(n) = cos(z(n-1)) + c  (Escape time)"
    override val pMin = -3.2
    override val pMax = 6.3
    override val qMin = -5.0
    override val qMax = 5.0

    override fun step(c: Complex, z: Complex) = z.cos + c

    @JvmStatic
    fun main(args: Array<String>) = render()
}