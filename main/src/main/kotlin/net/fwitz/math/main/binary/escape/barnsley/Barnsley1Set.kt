package net.fwitz.math.main.binary.escape.barnsley

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The same fractal as [Barnsley1], but presented as a Mandelbrot-like parameter space map.
 */
object Barnsley1Set : AbstractEscapeTimeMapPlot() {
    override val title = "Barnsley1 Map  (Escape Time)"
    override val pMin = -2.0
    override val pMax = 2.0
    override val qMin = -2.0
    override val qMax = 2.0

    override fun step(c: Complex, z: Complex) = when {
        z.x >= 0 -> (z - 1) * c
        else -> (z + 1) * c
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}