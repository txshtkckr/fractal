package net.fwitz.math.main.ifs

import net.fwitz.math.fractal.ifs.Ifs
import net.fwitz.math.plot.canvas.CanvasPlot

object IfsFern {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.ifs(Ifs.FERN_LEAF).render()
}