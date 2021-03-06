package net.fwitz.math.main.ifs

import net.fwitz.math.fractal.ifs.Ifs
import net.fwitz.math.plot.canvas.CanvasPlot

object IfsTree {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.ifs(Ifs.TREE).render()
}