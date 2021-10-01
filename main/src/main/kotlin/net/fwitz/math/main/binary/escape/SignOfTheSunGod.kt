package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimePaletteFunction
import net.fwitz.math.plot.renderer.palette.EscapeTimePalette
import net.fwitz.math.plot.renderer.palette.Palette
import net.fwitz.math.plot.renderer.palette.PaletteVGA256

// https://www.flickr.com/photos/fractal_ken/3476756762
object SignOfTheSunGod: AbstractEscapeTimeMapPlot(iters = 50) {
    override val title = "z(n) = 1 / (z(n-1) + z(0))^2 (Escape time)"
    override val pMin = -2.0
    override val pMax = 1.0
    override val qMin = -1.5
    override val qMax = 1.5

    override fun palette() = PaletteVGA256.WithoutLast8
    override fun init(c: Complex) = Complex.ZERO
    override fun step(c: Complex, z: Complex) = (z + c).pow(-2)

    @JvmStatic
    fun main(args: Array<String>) = render()
}