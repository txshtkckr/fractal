package net.fwitz.math.main.ifs

import net.fwitz.math.fractal.ifs.Ifs
import net.fwitz.math.plot.canvas.CanvasPlot

object IfsSierpinskiTriangle {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.ifs(Ifs.SIERPINSKI_TRIANGLE).render()
}