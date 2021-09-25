package net.fwitz.math.main.ifs

import net.fwitz.math.fractal.ifs.Ifs
import net.fwitz.math.plot.canvas.CanvasPlot

object IfsSierpinskiCarpet {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.ifs(Ifs.SIERPINSKI_CARPET).render()
}