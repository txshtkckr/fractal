package net.fwitz.math.main.binary.escape.barnsley

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.AbstractEscapeTimeMapPlot

/**
 * The same fractal as [Barnsley2], but presented as a Mandelbrot-like parameter space map.
 */
object Barnsley2Set : AbstractEscapeTimeMapPlot() {
    override val title = "Barnsley2 Map  (Escape Time)"
    override val pMin = -2.0
    override val pMax = 2.0
    override val qMin = -2.0
    override val qMax = 2.0

    override fun step(c: Complex, z: Complex): Complex {
        val cz = c * z
        return when {
            cz.y >= 0 -> cz - c
            else -> cz + c
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = render()
}