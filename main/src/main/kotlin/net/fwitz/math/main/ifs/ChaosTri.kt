package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosTri {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Triangle (Chaos)",
        n = 3,
        r = 0.4
    ).render()
}