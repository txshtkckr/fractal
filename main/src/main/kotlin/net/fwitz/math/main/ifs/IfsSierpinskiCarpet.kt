package net.fwitz.math.main.ifs

import net.fwitz.math.fractal.ifs.Ifs
import net.fwitz.math.plot.canvas.CanvasPlot
import net.fwitz.math.plot.renderer.palette.PaletteCustom
import java.awt.Color

object IfsSierpinskiCarpet {
    private val palette = PaletteCustom(
        Color.BLACK,
        Color.RED,
        Color.ORANGE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color(0x80, 0x00, 0xFF),
        Color.MAGENTA
    )

    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.ifs(Ifs.SIERPINSKI_CARPET, palette).render()
}