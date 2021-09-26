package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosSquare {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Squares (Chaos)",
        n = 4
    ).render()
}