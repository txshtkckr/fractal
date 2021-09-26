package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosPent {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Pentagons (Chaos)",
        n = 5
    ).render()
}