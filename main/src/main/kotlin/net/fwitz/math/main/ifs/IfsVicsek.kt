package net.fwitz.math.main.ifs

import net.fwitz.math.fractal.ifs.Ifs
import net.fwitz.math.plot.canvas.CanvasPlot

object IfsVicsek {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.ifs(Ifs.VICSEK).render()
}