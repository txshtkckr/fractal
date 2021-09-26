package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosDodec {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Dodecagon (Chaos)",
        n = 12
    ).render()
}