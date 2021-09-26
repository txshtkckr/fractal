package net.fwitz.math.main.ifs

import net.fwitz.math.fractal.ifs.Ifs
import net.fwitz.math.plot.canvas.CanvasPlot
import net.fwitz.math.plot.renderer.palette.PaletteCustom
import java.awt.Color

object IfsFern {
    val palette = PaletteCustom(
        Color.BLACK,
        Color.MAGENTA,
        Color.CYAN,
        Color.YELLOW,
        Color.GREEN
    )
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.ifs(Ifs.FERN_LEAF, palette).render()
}